package com.microservice.erp.services.definition;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.microservice.erp.domain.dto.ResetPasswordDto;
import com.microservice.erp.domain.dto.UserProfileDto;
import org.springframework.http.ResponseEntity;

import java.text.ParseException;

public interface ISecuritySettingService {

    ResponseEntity<?> getUserInfoByCidAndDob(UserProfileDto userProfileDto) throws ParseException;

    ResponseEntity<?> receiveResetPasswordOtp(UserProfileDto userProfileDto) throws ParseException, JsonProcessingException;

    ResponseEntity<?> verifyResetPwOtp(UserProfileDto userProfileDto);

    ResponseEntity<?> resetPasswordUsingMobileNo(UserProfileDto userProfileDto);

    ResponseEntity<?> getPasswordResetLink(ResetPasswordDto resetPasswordDto) throws Exception;

    ResponseEntity<?> validatePasswordResetLink(ResetPasswordDto resetPasswordDto);

    ResponseEntity<?> resetPasswordUsingEmail(ResetPasswordDto resetPasswordDto);

    ResponseEntity<?> changePassword(UserProfileDto userProfileDto);

    ResponseEntity<?> resetUserPassword(UserProfileDto userProfileDto);


}
