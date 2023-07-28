package com.microservice.erp.services.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.microservice.erp.domain.dao.SecuritySettingDao;
import com.microservice.erp.domain.dto.MessageResponse;
import com.microservice.erp.domain.dto.ResetPasswordDto;
import com.microservice.erp.domain.dto.UserProfileDto;
import com.microservice.erp.domain.entities.PwResetOtp;
import com.microservice.erp.domain.entities.PwResetUrl;
import com.microservice.erp.domain.entities.User;
import com.microservice.erp.domain.eventListener.AddToQueue;
import com.microservice.erp.domain.eventListener.EventBus;
import com.microservice.erp.domain.helper.OtpGenerator;
import com.microservice.erp.domain.repositories.IPwResetOtpRepository;
import com.microservice.erp.domain.repositories.IPwResetUrlRepository;
import com.microservice.erp.domain.repositories.UserRepository;
import com.microservice.erp.services.definition.ISecuritySettingService;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;

@Service
public class SecuritySettingService implements ISecuritySettingService {
    private final UserRepository userRepository;
    private final IPwResetUrlRepository iPwResetUrlRepository;
    private final PasswordEncoder encoder;
    private final AddToQueue addToQueue;
    private final SecuritySettingDao securitySettingDao;
    private final IPwResetOtpRepository iPwResetOtpRepository;

    public SecuritySettingService(UserRepository userRepository, IPwResetUrlRepository iPwResetUrlRepository, PasswordEncoder encoder, AddToQueue addToQueue, SecuritySettingDao securitySettingDao, IPwResetOtpRepository iPwResetOtpRepository) {
        this.userRepository = userRepository;
        this.iPwResetUrlRepository = iPwResetUrlRepository;
        this.encoder = encoder;
        this.addToQueue = addToQueue;
        this.securitySettingDao = securitySettingDao;
        this.iPwResetOtpRepository = iPwResetOtpRepository;
    }

    @Override
    public ResponseEntity<?> getUserInfoByCidAndDob(UserProfileDto userProfileDto) throws ParseException {
        String cid = userProfileDto.getCid();
        Date dob = new SimpleDateFormat("dd/MM/yyyy").parse(userProfileDto.getBirthDate());
        User userDb = userRepository.findByCidAndDob(cid, dob);
        if (userDb == null) {
            return ResponseEntity.badRequest().body(new MessageResponse("CID or DOB is incorrect."));
        } else {
            return ResponseEntity.ok(userDb);
        }
    }

    @Override
    public ResponseEntity<?> receiveResetPasswordOtp(UserProfileDto userProfileDto) throws ParseException, JsonProcessingException {
        BigInteger userId = userProfileDto.getUserId();
        String cid = userProfileDto.getCid();
        String enteredMobileNumber = userProfileDto.getEnteredMobileNumber();
        Date dob = userProfileDto.getDob();

        User userDb = userRepository.findByUserIdAndCidAndDobAndMobileNo(userId, cid, dob, enteredMobileNumber);
        if (userDb == null) {
            return ResponseEntity.badRequest().body(new MessageResponse("Mobile number does not match your account."));
        } else {
            String otp = OtpGenerator.generateOtp();
            PwResetOtp pwResetOtp = new ModelMapper().map(userDb, PwResetOtp.class);
            pwResetOtp.setDate(new Date());
            pwResetOtp.setOtp(otp);
            pwResetOtp.setExpiryTime(180);
            pwResetOtp.setCreatedBy(userId);
            pwResetOtp.setCreatedDate(new Date());
            PwResetOtp pwResetOtpDb = iPwResetOtpRepository.findByUserId(userId);
            if (pwResetOtpDb != null) {
                iPwResetOtpRepository.deleteById(userId);
            }
            iPwResetOtpRepository.save(pwResetOtp);
            String messageSms = "Dear User, the OTP to reset password in Gyalsung System is " + otp + ".Please use this OTP within 3 minutes.";
            EventBus eventBusSms = EventBus.withId(null, null, null, messageSms, null, enteredMobileNumber);
            addToQueue.addToQueue("sms", eventBusSms);
            return ResponseEntity.ok("OTP sent to " + enteredMobileNumber);
        }
    }

    @Override
    public ResponseEntity<?> verifyResetPwOtp(UserProfileDto userProfileDto) {
        BigInteger userId = userProfileDto.getUserId();
        String enteredMobileNumber = userProfileDto.getEnteredMobileNumber();
        String otp = userProfileDto.getOtp();
        PwResetOtp pwResetOtpDb = iPwResetOtpRepository.findByUserIdAndMobileNoAndOtp(userId, enteredMobileNumber, otp);
        if (pwResetOtpDb == null) {
            return ResponseEntity.badRequest().body(new MessageResponse("OTP didn't match."));
        } else {
            return ResponseEntity.ok("OTP verified.");
        }
    }

    @Override
    public ResponseEntity<?> resetPasswordUsingMobileNo(UserProfileDto userProfileDto) {
        BigInteger userId = userProfileDto.getUserId();
        String mobileNo = userProfileDto.getEnteredMobileNumber();
        String otp = userProfileDto.getOtp();
        if (Objects.isNull(userId)) {
            return ResponseEntity.badRequest().body(new MessageResponse("User does not exist."));
        }
        PwResetOtp pwResetOtpDb = iPwResetOtpRepository.findByUserIdAndMobileNoAndOtp(userId, mobileNo, otp);
        if (pwResetOtpDb == null) {//no information found in otp table
            return ResponseEntity.badRequest().body(new MessageResponse("Something went wrong. Please try again"));
        }
        if (!pwResetOtpDb.getOtp().equals(otp)) {//OTP miss match
            return ResponseEntity.badRequest().body(new MessageResponse("Something went wrong. Please try again"));
        }
        //confirm current pw must be equal to pw
        if (!Objects.equals(userProfileDto.getNewPassword(), userProfileDto.getConfirmPassword())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Confirm password doesn't match."));
        }
        Optional<User> userDb = userRepository.findByUserId(userId);
        String pw = encoder.encode(userProfileDto.getNewPassword());
        BigInteger id = userDb.get().getId();
        securitySettingDao.updatePassword(pw, id);
        iPwResetOtpRepository.deleteById(pwResetOtpDb.getUserId());
        return ResponseEntity.ok(new MessageResponse("Password changed successfully."));
    }

    @Override
    public ResponseEntity<?> getPasswordResetLink(ResetPasswordDto resetPasswordDto) throws Exception {
        BigInteger userId = resetPasswordDto.getUserId();
        String email = resetPasswordDto.getEmail();
        User userDb = userRepository.findByUserIdAndEmail(userId, email);
        if (userDb == null) {
            return ResponseEntity.badRequest().body(new MessageResponse("We couldn't find an account associated with " + email + ". Please try with different email."));
        }
        PwResetUrl pwResetUrl = new PwResetUrl();
        pwResetUrl.setUserId(userId);
        pwResetUrl.setEmail(resetPasswordDto.getEmail());
        pwResetUrl.setDate(new Date());
        pwResetUrl.setCreatedBy(userDb.getUserId());
        pwResetUrl.setCreatedDate(new Date());
        iPwResetUrlRepository.save(pwResetUrl);
        sendMail(pwResetUrl, resetPasswordDto.getDomainName());
        return ResponseEntity.ok(new MessageResponse("A password reset link was sent to your email. Please check your email inbox. If there is no mail in your inbox, please check your spam folder."));
    }

    @Override
    public ResponseEntity<?> validatePasswordResetLink(ResetPasswordDto resetPasswordDto) {
        BigInteger userId = resetPasswordDto.getRequestIdFromUrl();
        String email = resetPasswordDto.getEmailFromUrl();
        PwResetUrl pwResetUrl = iPwResetUrlRepository.findByUserIdAndEmail(userId, email);
        if (pwResetUrl == null) {
            return ResponseEntity.badRequest().body(new MessageResponse("Invalid link. Please try again."));
        } else {
            return ResponseEntity.ok(pwResetUrl);
        }
    }

    @Override
    public ResponseEntity<?> resetPasswordUsingEmail(ResetPasswordDto resetPasswordDto) {
        PwResetUrl pwResetUrl = iPwResetUrlRepository.findByUserIdAndEmail(resetPasswordDto.getUserId(),resetPasswordDto.getEmail());
        if (pwResetUrl != null) {
            User userDb = userRepository.findByUserIdAndEmail(resetPasswordDto.getUserId(), resetPasswordDto.getEmail());
            if (userDb == null) {
                return ResponseEntity.badRequest().body(new MessageResponse("Something went wrong. Please try again."));
            }
            String pw = encoder.encode(resetPasswordDto.getPassword());
            BigInteger userId = userDb.getUserId();
            BigInteger id = userDb.getId();
            securitySettingDao.updatePassword(pw, id);
            iPwResetUrlRepository.deleteById(userId);
            return ResponseEntity.ok(new MessageResponse("Password changed successfully. Please log in using new password."));
        } else {
            return ResponseEntity.ok().body(new MessageResponse("Something went wrong. Please try again."));
        }
    }

    @Override
    public ResponseEntity<?> changePassword(UserProfileDto userProfileDto) {
        if (Objects.isNull(userProfileDto.getUserId())) {
            return ResponseEntity.badRequest().body(new MessageResponse("User does not exist."));
        }

        Optional<User> userDb = userRepository.findByUserId(userProfileDto.getUserId());
        User user = new ModelMapper().map(userDb, User.class);

        //current pw must be equal to existing pw
        String curPw = userProfileDto.getCurrentPassword();
        if (!encoder.matches(curPw, userDb.get().getPassword())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Current password doesn't match."));
        }

        //confirm current pw must be equal to pw
        if (!Objects.equals(userProfileDto.getNewPassword(), userProfileDto.getConfirmPassword())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Confirm password doesn't match."));
        }

        user.setPassword(encoder.encode(userProfileDto.getNewPassword()));
        userRepository.save(user);
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


    private void sendMail(PwResetUrl pwResetUrl, String domainName) throws Exception {
        BigInteger userId = pwResetUrl.getUserId();
        String url = domainName + "/resetPassword?id=" + userId + "&email=" + pwResetUrl.getEmail();
        String toAddress = pwResetUrl.getEmail();
        String subject = "Gyalsung System Password Reset";
        String message = "Sir/Madam, <br><br>" + "Please click below link to reset your password of Gyalsung System:<br>" + "<a href='" + url + "' target='_blank'>Click here</a><br><br>" + "<br><br>Thank you<br>" + "Have a good day.<br><br><br>" + "<small>****** This is a system generated e-mail. Please do not reply ******</small><br><br><br>";
        EventBus eventBus = EventBus.withId(toAddress, null, null, message, subject, null);
        addToQueue.addToQueue("email", eventBus);
    }


}
