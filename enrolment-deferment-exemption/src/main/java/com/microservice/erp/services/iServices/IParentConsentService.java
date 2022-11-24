package com.microservice.erp.services.iServices;

import com.microservice.erp.domain.dto.ParentConsentDto;
import org.springframework.http.ResponseEntity;

public interface IParentConsentService {
    ResponseEntity<?> receiveOtp(ParentConsentDto parentConsentDto) throws Exception;

    ResponseEntity<?> submitParentConsent(ParentConsentDto parentConsentDto) throws Exception;

    ResponseEntity<?> getParentConsentList(String authHeader, String year, Character status);
}
