package com.microservice.erp.services.iServices;

import com.microservice.erp.domain.dto.MedicalBookingDto;
import org.springframework.http.ResponseEntity;

public interface IMedicalBookingService {
    ResponseEntity<?> bookMedicalAppointment(MedicalBookingDto medicalBookingDto);
}
