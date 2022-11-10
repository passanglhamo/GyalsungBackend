package com.microservice.erp.services.iServices;

import org.springframework.http.ResponseEntity;

import java.math.BigInteger;

public interface IMedicalBookingListService {
    ResponseEntity<?> getAllBookingDateByHospitalIdAndYear(BigInteger hospitalId, BigInteger year);

    ResponseEntity<?> getTimeSlotsByScheduleDateId(BigInteger hospitalScheduleDateId);
}
