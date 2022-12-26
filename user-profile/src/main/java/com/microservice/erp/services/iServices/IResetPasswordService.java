package com.microservice.erp.services.iServices;

import com.microservice.erp.domain.dto.ResetPasswordDto;
import org.springframework.http.ResponseEntity;

public interface IResetPasswordService {
    ResponseEntity<?> requestPasswordChange(ResetPasswordDto resetPasswordDto) throws Exception;

    ResponseEntity<?> validatePasswordResetLink(ResetPasswordDto resetPasswordDto);

    ResponseEntity<?> resetPassword(ResetPasswordDto resetPasswordDto);
}
