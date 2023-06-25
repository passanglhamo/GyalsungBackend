package com.microservice.erp.services.iServices;

import com.microservice.erp.domain.dto.ExemptionDto;
import com.microservice.erp.domain.dto.ExemptionListDto;
import org.springframework.http.ResponseEntity;

import java.math.BigInteger;
import java.util.List;

public interface IReadExemptionService {
    List<ExemptionDto> getAllExemptionList(String authHeader);

    List<ExemptionListDto> getExemptionListByCriteria(String authHeader, String exemptionYear, Character status, BigInteger reasonId, Character gender, String cid, String caseNumber);

    ResponseEntity<?> getExemptionByUserId(BigInteger userId);

    ResponseEntity<?> getExemptionValidation(BigInteger userId);

    ResponseEntity<?> getExemptionListByUserId(BigInteger userId);
}
