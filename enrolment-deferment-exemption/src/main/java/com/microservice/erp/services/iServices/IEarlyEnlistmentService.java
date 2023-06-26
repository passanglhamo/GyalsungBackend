package com.microservice.erp.services.iServices;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.microservice.erp.domain.dto.DefermentListDto;
import com.microservice.erp.domain.dto.EarlyEnlistmentDto;
import com.microservice.erp.domain.dto.GuardianConsentRequestDto;
import org.springframework.http.ResponseEntity;

import java.math.BigInteger;
import java.util.List;

public interface IEarlyEnlistmentService {
    ResponseEntity<?> checkAgeValidation(String authHeader, EarlyEnlistmentDto earlyEnlistmentDto);

    ResponseEntity<?> requestGuardianConsent(String authHeader, GuardianConsentRequestDto guardianConsentRequestDto) throws JsonProcessingException;

    ResponseEntity<?> getGuardianConsentStatus(BigInteger userId);

    ResponseEntity<?> applyEarlyEnlistment(String authHeader, EarlyEnlistmentDto earlyEnlistmentDto) throws JsonProcessingException;

    ResponseEntity<?> getEarlyEnlistmentStatus(BigInteger userId);

    List<DefermentListDto> getEarlyEnlistmentListByCriteria(String authHeader, Character status, Character gender, String cid);
}
