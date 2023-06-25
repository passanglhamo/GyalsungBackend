package com.microservice.erp.services.iServices;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.microservice.erp.domain.dto.GuardianConsentRequestDto;
import org.springframework.http.ResponseEntity;

import java.math.BigInteger;

public interface IEarlyEnlistmentService {
    ResponseEntity<?> requestGuardianConsent(String authHeader, GuardianConsentRequestDto guardianConsentRequestDto) throws JsonProcessingException;

    ResponseEntity<?> getGuardianConsentStatus(BigInteger userId);

    ResponseEntity<?> applyEarlyEnlistment(String authHeader, BigInteger userId) throws JsonProcessingException;
}
