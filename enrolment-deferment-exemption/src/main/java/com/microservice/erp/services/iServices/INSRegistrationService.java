package com.microservice.erp.services.iServices;

import com.microservice.erp.domain.dto.NSRegistrationDto;
import org.springframework.http.ResponseEntity;

import java.math.BigInteger;

public interface INSRegistrationService {
    ResponseEntity<?> save(String authHeader, NSRegistrationDto nsRegistrationDto) throws Exception;

    ResponseEntity<?> getMyRegistrationInfo(BigInteger userId);
}
