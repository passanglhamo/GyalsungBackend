package com.microservice.erp.services.iServices;

import com.microservice.erp.domain.dto.EarlyEnlistmentMedBookingDto;
import org.springframework.http.ResponseEntity;

import java.math.BigInteger;

public interface IEarlyEnlistmentMedicalBookingService {
    ResponseEntity<?> bookMedicalAppointment(String authHeader, BigInteger currentUserId, EarlyEnlistmentMedBookingDto earlyEnlistmentMedBookingDto) throws Exception;

    ResponseEntity<?> getEarlyEnlistMedBookingByUserId(BigInteger userId, BigInteger earlyEnlistmentId);
}
