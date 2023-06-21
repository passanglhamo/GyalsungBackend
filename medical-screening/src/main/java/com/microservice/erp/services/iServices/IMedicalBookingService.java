package com.microservice.erp.services.iServices;

import com.microservice.erp.domain.dto.HospitalBookingDetailsDto;
import com.microservice.erp.domain.dto.MedicalBookingDto;
import org.springframework.http.ResponseEntity;

import java.math.BigInteger;
import java.text.ParseException;

public interface IMedicalBookingService {
    ResponseEntity<?> bookMedicalAppointment(String authHeader, MedicalBookingDto medicalBookingDto) throws Exception;

    ResponseEntity<?> getMedicalAppointmentDetail(String authHeader, BigInteger userId);

    ResponseEntity<?> changeMedicalAppointment(String authHeader, MedicalBookingDto medicalBookingDto) throws Exception;

    ResponseEntity<?> getPreviousSelfDeclaration(String authHeader, BigInteger userId);

    ResponseEntity<?> resubmitSelfDeclaration(MedicalBookingDto medicalBookingDto);

    ResponseEntity<?> bookHospitalAppointment(String authHeader, HospitalBookingDetailsDto hospitalBookingDetailsDto);
}
