package com.microservice.erp.services.impl;

import com.microservice.erp.domain.dto.EventBus;
import com.microservice.erp.domain.dto.MessageResponse;
import com.microservice.erp.domain.dto.ResetPasswordDto;
import com.microservice.erp.domain.entities.PwChangeRequest;
import com.microservice.erp.domain.entities.SaUser;
import com.microservice.erp.domain.repositories.IRequestPasswordChangeRepository;
import com.microservice.erp.domain.repositories.ISaUserRepository;
import com.microservice.erp.services.iServices.IResetPasswordService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.Date;

@Service
@AllArgsConstructor
public class ResetPasswordService implements IResetPasswordService {
    private final IRequestPasswordChangeRepository requestPasswordChangeRepository;
    private final ISaUserRepository userRepository;
    private final PasswordEncoder encoder;
    private final AddToQueue addToQueue;

    public ResponseEntity<?> requestPasswordChange(ResetPasswordDto resetPasswordDto) throws Exception {
        SaUser userDb = userRepository.findByEmail(resetPasswordDto.getEmail());
        if (userDb == null) {
            return ResponseEntity.badRequest().body(new MessageResponse("We couldn't find an account associated with " + resetPasswordDto.getEmail() + ". Please try with different email."));
        }
        PwChangeRequest pwChangeRequest = new PwChangeRequest();
        pwChangeRequest.setEmail(resetPasswordDto.getEmail());
        pwChangeRequest.setStatus('P');//P =  Requested, C = Changed
        pwChangeRequest.setCreatedBy(userDb.getId());
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

    public ResponseEntity<?> resetPassword(ResetPasswordDto resetPasswordDto) {
        PwChangeRequest pwChangeRequest = requestPasswordChangeRepository.findByRequestId(resetPasswordDto.getRequestId());
        if (pwChangeRequest != null) {
            String emailDb = pwChangeRequest.getEmail();
            String emailFront = resetPasswordDto.getEmail();
            if (!emailDb.equals(emailFront)) {
                return ResponseEntity.badRequest().body(new MessageResponse("Something went wrong. Please try again."));
            } else {
                SaUser userDb = userRepository.findByEmail(resetPasswordDto.getEmail());
                if (userDb == null) {
                    return ResponseEntity.badRequest().body(new MessageResponse("Something went wrong. Please try again."));
                }
                if (pwChangeRequest.getStatus() != 'P') {
                    return ResponseEntity.badRequest().body(new MessageResponse("You have already changed password using this link. Please request new link."));
                }
                SaUser user = new ModelMapper().map(userDb, SaUser.class);
                user.setPassword(encoder.encode(resetPasswordDto.getPassword()));
                userRepository.save(user);

                pwChangeRequest.setStatus('C');//P =  Requested, C = Changed
                pwChangeRequest.setUpdatedBy(userDb.getId());
                pwChangeRequest.setUpdatedDate(new Date());
                requestPasswordChangeRepository.save(pwChangeRequest);
                return ResponseEntity.ok(new MessageResponse("Password changed successfully. Please log in using new password."));
            }
        } else {
            return ResponseEntity.ok().body(new MessageResponse("Something went wrong. Please try again."));
        }
    }

}
