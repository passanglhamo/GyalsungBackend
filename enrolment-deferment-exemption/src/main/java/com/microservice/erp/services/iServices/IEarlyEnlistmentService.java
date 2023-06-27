package com.microservice.erp.services.iServices;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.microservice.erp.domain.dto.EarlyEnlistmentDto;
import com.microservice.erp.domain.dto.GuardianConsentRequestDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.ResponseEntity;

import javax.validation.Valid;
import java.math.BigInteger;
import java.util.List;

public interface IEarlyEnlistmentService {
    ResponseEntity<?> checkAgeValidation(String authHeader, EarlyEnlistmentDto earlyEnlistmentDto);

    ResponseEntity<?> requestGuardianConsent(String authHeader, GuardianConsentRequestDto guardianConsentRequestDto) throws JsonProcessingException;

    ResponseEntity<?> getGuardianConsentStatus(BigInteger userId);

    ResponseEntity<?> applyEarlyEnlistment(String authHeader, EarlyEnlistmentDto earlyEnlistmentDto) throws JsonProcessingException;

    ResponseEntity<?> getEarlyEnlistmentStatus(BigInteger userId);

    List<EarlyEnlistmentDto> getEarlyEnlistmentListByCriteria(String authHeader, String enlistmentYear, Character status, Character gender, String cid, Character parentConsentStatus, Integer dzongkhagId);

    ResponseEntity<?> approveRejectById(String authHeader, @Valid UpdateEarlyEnlistmentCommand command);

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    class UpdateEarlyEnlistmentCommand {
        private String enlistmentYear;
        private String remarks;
        private String status;
        private BigInteger enlistmentId;
    }
}
