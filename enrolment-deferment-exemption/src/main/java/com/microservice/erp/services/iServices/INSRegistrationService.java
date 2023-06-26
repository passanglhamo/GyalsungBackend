package com.microservice.erp.services.iServices;

import com.microservice.erp.domain.dto.DefermentListDto;
import com.microservice.erp.domain.dto.NSRegistrationDto;
import org.springframework.http.ResponseEntity;

import java.math.BigInteger;
import java.util.List;

public interface INSRegistrationService {
    ResponseEntity<?> save(String authHeader, NSRegistrationDto nsRegistrationDto) throws Exception;

    ResponseEntity<?> getMyRegistrationInfo(BigInteger userId);

    List<NSRegistrationDto> getRegistrationListByCriteria(String authHeader, String enlistmentYear,Character status, Character gender, String cid);
}
