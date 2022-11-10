package com.microservice.erp.services.iServices;

import org.springframework.http.ResponseEntity;

public interface IMedicalBookingListService {
    ResponseEntity<?> getAllBookingByHospitalIdAndYear(Long hospitalId, String year);
}
