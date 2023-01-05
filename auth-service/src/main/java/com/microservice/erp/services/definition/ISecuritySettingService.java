package com.microservice.erp.services.definition;

import com.microservice.erp.domain.dto.ResetPasswordDto;
import com.microservice.erp.domain.dto.UserProfileDto;
import org.springframework.http.ResponseEntity;

public interface ISecuritySettingService {
    ResponseEntity<?> changePassword(UserProfileDto userProfileDto);

    ResponseEntity<?> changeUsername(UserProfileDto userProfileDto);

    ResponseEntity<?> resetUserPassword(UserProfileDto userProfileDto);

    ResponseEntity<?> requestPasswordChange(ResetPasswordDto resetPasswordDto) throws Exception;

    ResponseEntity<?> validatePasswordResetLink(ResetPasswordDto resetPasswordDto);

    ResponseEntity<?> resetPassword(ResetPasswordDto resetPasswordDto);
}
