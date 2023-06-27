package com.microservice.erp.services.iServices;

import com.microservice.erp.domain.dto.GuardianConsentDto;
import org.springframework.http.ResponseEntity;

public interface IGuardianConsentService {
    ResponseEntity<?> validateGuardianConsentLink(GuardianConsentDto guardianConsentDto);

    ResponseEntity<?> validateGuardian(GuardianConsentDto guardianConsentDto);
}
