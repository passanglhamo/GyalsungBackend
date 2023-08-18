package com.microservice.erp.services.iServices;

import com.microservice.erp.domain.dto.EarlyEnlistmentMedBookingDto;
import org.springframework.http.ResponseEntity;

import java.math.BigInteger;

public interface IEarlyEnlistmentMedicalBookingService {
    ResponseEntity<?> save(String authHeader,EarlyEnlistmentMedBookingDto earlyEnlistmentMedBookingDto);

    ResponseEntity<?> getEarlyEnlistMedBookingById(BigInteger earlyEnlistmentId);
}
