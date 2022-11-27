package com.microservice.erp.services.iServices;

import com.microservice.erp.domain.dto.ExemptionDto;
import org.springframework.http.ResponseEntity;

import java.math.BigInteger;
import java.util.List;

public interface IReadExemptionService {
    List<ExemptionDto> getAllExemptionList(String authHeader);

    List<ExemptionDto> getExemptionListByStatus(String authHeader, Character status);

    ResponseEntity<?> getExemptionByUserId(BigInteger userId);

    ResponseEntity<?> getExemptionValidation(BigInteger userId);
}
