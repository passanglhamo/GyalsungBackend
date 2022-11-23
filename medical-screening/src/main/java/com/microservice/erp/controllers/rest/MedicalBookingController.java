package com.microservice.erp.controllers.rest;

import com.microservice.erp.domain.dto.MedicalBookingDto;
import com.microservice.erp.services.iServices.IMedicalBookingService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;

@RestController
@AllArgsConstructor
@RequestMapping("/medicalBooking")
public class MedicalBookingController {
    private IMedicalBookingService iMedicalBookingService;

    @PostMapping(value = "/bookMedicalAppointment")
    public ResponseEntity<?> bookMedicalAppointment(@RequestHeader("Authorization") String authHeader, @RequestBody MedicalBookingDto medicalBookingDto) throws Exception {
        return iMedicalBookingService.bookMedicalAppointment(authHeader, medicalBookingDto);
    }

    @GetMapping(value = "/getMedicalAppointmentDetail")
    public ResponseEntity<?> getMedicalAppointmentDetail(@RequestHeader("Authorization") String authHeader, @RequestParam("userId") BigInteger userId) {
        return iMedicalBookingService.getMedicalAppointmentDetail(authHeader, userId);
    }

    @PostMapping(value = "/changeMedicalAppointment")
    public ResponseEntity<?> changeMedicalAppointment(@RequestHeader("Authorization") String authHeader, @RequestBody MedicalBookingDto medicalBookingDto) throws Exception {
        return iMedicalBookingService.changeMedicalAppointment(authHeader, medicalBookingDto);
    }

    @GetMapping(value = "/getPreviousSelfDeclaration")
    public ResponseEntity<?> getPreviousSelfDeclaration(@RequestHeader("Authorization") String authHeader, @RequestParam("userId") BigInteger userId) {
        return iMedicalBookingService.getPreviousSelfDeclaration(authHeader, userId);
    }

    @PostMapping(value = "/resubmitSelfDeclaration")
    public ResponseEntity<?> resubmitSelfDeclaration(@RequestBody MedicalBookingDto medicalBookingDto) {
        return iMedicalBookingService.resubmitSelfDeclaration(medicalBookingDto);
    }
}
