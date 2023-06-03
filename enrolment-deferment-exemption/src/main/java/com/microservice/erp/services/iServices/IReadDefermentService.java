package com.microservice.erp.services.iServices;

import com.microservice.erp.domain.dto.DefermentDto;
import org.springframework.http.ResponseEntity;

import java.math.BigInteger;
import java.util.List;

public interface IReadDefermentService {
    List<DefermentDto> getAllDefermentList(String authHeader);

    List<DefermentDto> getDefermentListByDefermentYearReasonStatus(String authHeader, String defermentYear, BigInteger reasonId, Character status, Character gender, String cid);

    ResponseEntity<?> getDefermentByUserId(BigInteger userId);

    ResponseEntity<?> getDefermentValidation(BigInteger userId);

    List<DefermentDto> getApprovedListByDefermentYearAndUserId(String authHeader, String defermentYear, BigInteger userId);

    ResponseEntity<?> getDefermentListByUserId(BigInteger userId);
}
