package com.microservice.erp.services.iServices.parentConsent;

import com.microservice.erp.domain.dto.parentConsent.ParentConsentDto;
import com.microservice.erp.domain.entities.ParentConsentOtp;
import org.springframework.http.ResponseEntity;

public interface IParentConsentService {
    ResponseEntity<?> receiveOtp(ParentConsentDto parentConsentDto);

    ResponseEntity<?> submitParentConsent(ParentConsentDto parentConsentDto);

    ResponseEntity<?> getParentConsentList();
}
