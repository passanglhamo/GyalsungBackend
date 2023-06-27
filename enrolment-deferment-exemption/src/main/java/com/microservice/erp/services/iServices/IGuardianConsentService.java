package com.microservice.erp.services.iServices;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.microservice.erp.domain.dto.GuardianConsentDto;
import org.springframework.http.ResponseEntity;

public interface IGuardianConsentService {
    ResponseEntity<?> validateGuardianConsentLink(GuardianConsentDto guardianConsentDto);

    ResponseEntity<?> validateGuardian(GuardianConsentDto guardianConsentDto);

    ResponseEntity<?> receiveOtp(GuardianConsentDto guardianConsentDto) throws JsonProcessingException;

    ResponseEntity<?> verifyOtp(GuardianConsentDto guardianConsentDto);

    ResponseEntity<?> grantGuardianConsent(GuardianConsentDto guardianConsentDto) throws JsonProcessingException;

    ResponseEntity<?> denyGuardianConsent(GuardianConsentDto guardianConsentDto) throws JsonProcessingException;
}
