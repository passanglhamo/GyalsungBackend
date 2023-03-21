package com.microservice.erp.services.impl;

import com.microservice.erp.domain.dao.SecuritySettingDao;
import com.microservice.erp.domain.dto.MessageResponse;
import com.microservice.erp.domain.dto.ResetPasswordDto;
import com.microservice.erp.domain.dto.UserProfileDto;
import com.microservice.erp.domain.entities.PwChangeRequest;
import com.microservice.erp.domain.entities.User;
import com.microservice.erp.domain.eventListener.AddToQueue;
import com.microservice.erp.domain.eventListener.EventBus;
import com.microservice.erp.domain.repositories.IRequestPasswordChangeRepository;
import com.microservice.erp.domain.repositories.UserRepository;
import com.microservice.erp.services.definition.ISecuritySettingService;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;

@Service
public class SecuritySettingService implements ISecuritySettingService {
    private final UserRepository userRepository;
    private final IRequestPasswordChangeRepository requestPasswordChangeRepository;
    private final PasswordEncoder encoder;
    private final AddToQueue addToQueue;
    private final SecuritySettingDao securitySettingDao;

    public SecuritySettingService(UserRepository userRepository, IRequestPasswordChangeRepository requestPasswordChangeRepository, PasswordEncoder encoder, AddToQueue addToQueue, SecuritySettingDao securitySettingDao) {
        this.userRepository = userRepository;
        this.requestPasswordChangeRepository = requestPasswordChangeRepository;
        this.encoder = encoder;
        this.addToQueue = addToQueue;
        this.securitySettingDao = securitySettingDao;
    }

    @Override
    public ResponseEntity<?> changePassword(UserProfileDto userProfileDto) {
        if (Objects.isNull(userProfileDto.getUserId())) {
            return ResponseEntity.badRequest().body(new MessageResponse("User does not exist."));
        }

        Optional<User> userDb = userRepository.findByUserId(userProfileDto.getUserId());
        User user = new ModelMapper().map(userDb, User.class);

        //current pw must be equal to existing pw
//        TODO: need to check pw match, matches method is not working. need to check one more time
//        String curPw = userProfileDto.getCurrentPassword();
//        if (!encoder.matches(userDb.get().getPassword(), curPw)) {
//            return ResponseEntity.badRequest().body(new MessageResponse("Current password doesn't match."));
//        }

        //confirm current pw must be equal to pw
        if (!Objects.equals(userProfileDto.getNewPassword(), userProfileDto.getConfirmPassword())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Confirm password doesn't match."));
        }

        user.setPassword(encoder.encode(userProfileDto.getNewPassword()));
        userRepository.save(user);
        //TODO: send email after changing password
        return ResponseEntity.ok(new MessageResponse("Password changed successfully."));
    }


    @Override
    public ResponseEntity<?> resetUserPassword(UserProfileDto userProfileDto) {
        if (Objects.isNull(userProfileDto.getUserId())) {
            return ResponseEntity.badRequest().body(new MessageResponse("User does not exist."));
        }
        User userDb = userRepository.findByUserId(userProfileDto.getUserId()).get();
        User user = new ModelMapper().map(userDb, User.class);
        //confirm current pw must be equal to new pw
        if (!Objects.equals(userProfileDto.getNewPassword(), userProfileDto.getConfirmPassword())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Confirm password doesn't match."));
        }
        user.setPassword(encoder.encode(userProfileDto.getNewPassword()));
        userRepository.save(user);

//        //TODO: send email after resetting password
        return ResponseEntity.ok(new MessageResponse("Password changed successfully."));
    }

    @Override
    public ResponseEntity<?> requestPasswordChange(ResetPasswordDto resetPasswordDto) throws Exception {
        Optional<User> userDb = userRepository.findByEmail(resetPasswordDto.getEmail());
        if (!userDb.isPresent()) {
            return ResponseEntity.badRequest().body(new MessageResponse("We couldn't find an account associated with " + resetPasswordDto.getEmail() + ". Please try with different email."));
        }
        PwChangeRequest pwChangeRequest = new PwChangeRequest();
        pwChangeRequest.setEmail(resetPasswordDto.getEmail());
        pwChangeRequest.setStatus('P');//P =  Requested, C = Changed
        pwChangeRequest.setCreatedBy(userDb.get().getUserId());
        pwChangeRequest.setCreatedDate(new Date());
        requestPasswordChangeRepository.save(pwChangeRequest);
        sendMail(pwChangeRequest, resetPasswordDto.getDomainName());
        return ResponseEntity.ok(new MessageResponse("Password reset link sent to your mail. Please check your mail."));
    }

    private void sendMail(PwChangeRequest pwChangeRequest, String domainName) throws Exception {
        BigInteger requestId = pwChangeRequest.getRequestId();
        String requestUrl = domainName + "/resetPassword?requestId=" + requestId + "&email=" + pwChangeRequest.getEmail();
        String toAddress = pwChangeRequest.getEmail();
        String subject = "Gyalsung System Password Reset";
        String message = "Sir/Madam, <br><br>" + "Please click below link to change your password of Gyalsung System:<br>" + "<a href='" + requestUrl + "' target='_blank'>Click here</a><br><br>" + "<br><br>Thank you<br>" + "Have a good day.<br><br><br>" + "<small>****** This is a system generated e-mail. Please do not reply ******</small>";
        EventBus eventBus = EventBus.withId(toAddress, null, null, message, subject, null);
        addToQueue.addToQueue("email", eventBus);
    }


    @Override
    public ResponseEntity<?> validatePasswordResetLink(ResetPasswordDto resetPasswordDto) {
        BigInteger requestId = resetPasswordDto.getRequestIdFromUrl();
        String email = resetPasswordDto.getEmailFromUrl();
        PwChangeRequest pwChangeRequest = requestPasswordChangeRepository.findByRequestIdAndEmail(requestId, email);
        if (pwChangeRequest == null) {
            return ResponseEntity.badRequest().body(new MessageResponse("Invalid link. Please try again."));
        } else {
            return ResponseEntity.ok(pwChangeRequest);
        }
    }

    @Override
    public ResponseEntity<?> resetPassword(ResetPasswordDto resetPasswordDto) {
        PwChangeRequest pwChangeRequest = requestPasswordChangeRepository.findByRequestId(resetPasswordDto.getRequestId());
        if (pwChangeRequest != null) {
            String emailDb = pwChangeRequest.getEmail();
            String emailFront = resetPasswordDto.getEmail();
            if (!emailDb.equals(emailFront)) {
                return ResponseEntity.badRequest().body(new MessageResponse("Something went wrong. Please try again."));
            } else {
                User userDb = userRepository.findByEmail(resetPasswordDto.getEmail()).get();
                if (userDb == null) {
                    return ResponseEntity.badRequest().body(new MessageResponse("Something went wrong. Please try again."));
                }
                if (pwChangeRequest.getStatus() != 'P') {
                    return ResponseEntity.badRequest().body(new MessageResponse("You have already changed password using this link. Please request new link."));
                }
                String pw = encoder.encode(resetPasswordDto.getPassword());
                BigInteger id = userDb.getId();
                securitySettingDao.updatePassword(pw, id);

                pwChangeRequest.setStatus('C');//P =  Requested, C = Changed
                pwChangeRequest.setUpdatedBy(userDb.getUserId());
                pwChangeRequest.setUpdatedDate(new Date());
                requestPasswordChangeRepository.save(pwChangeRequest);
                return ResponseEntity.ok(new MessageResponse("Password changed successfully. Please log in using new password."));
            }
        } else {
            return ResponseEntity.ok().body(new MessageResponse("Something went wrong. Please try again."));
        }
    }

}
