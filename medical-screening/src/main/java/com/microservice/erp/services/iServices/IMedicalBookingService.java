package com.microservice.erp.services.iServices;

import com.microservice.erp.domain.dto.MedicalBookingDto;
import org.springframework.http.ResponseEntity;

import java.text.ParseException;

public interface IMedicalBookingService {
    ResponseEntity<?> bookMedicalAppointment(String authHeader, MedicalBookingDto medicalBookingDto) throws Exception;

    ResponseEntity<?> getMedicalAppointmentDetail(String authHeader, Long userId);

    ResponseEntity<?> changeMedicalAppointment(String authHeader, MedicalBookingDto medicalBookingDto) throws Exception;

    ResponseEntity<?> getPreviousSelfDeclaration(String authHeader, Long userId);

    ResponseEntity<?> resubmitSelfDeclaration(MedicalBookingDto medicalBookingDto);
}
