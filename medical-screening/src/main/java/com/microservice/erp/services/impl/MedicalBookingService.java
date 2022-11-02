package com.microservice.erp.services.impl;

import com.microservice.erp.domain.dto.MedicalBookingDto;
import com.microservice.erp.services.iServices.IMedicalBookingService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class MedicalBookingService implements IMedicalBookingService {
    @Override
    public ResponseEntity<?> bookMedicalAppointment(MedicalBookingDto medicalBookingDto) {
        return null;
    }
}
