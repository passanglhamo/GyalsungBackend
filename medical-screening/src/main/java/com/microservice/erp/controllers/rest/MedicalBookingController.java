package com.microservice.erp.controllers.rest;

import com.microservice.erp.domain.dto.MedicalBookingDto;
import com.microservice.erp.services.iServices.IMedicalBookingService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@AllArgsConstructor
@RequestMapping("/medicalBooking")
public class MedicalBookingController {
    private IMedicalBookingService iMedicalBookingService;

    @PostMapping(value = "/bookMedicalAppointment")
    public ResponseEntity<?> bookMedicalAppointment(@RequestBody MedicalBookingDto medicalBookingDto) {
        return iMedicalBookingService.bookMedicalAppointment(medicalBookingDto);
    }

    @GetMapping(value = "/getMedicalAppointmentDetail")
    public ResponseEntity<?> getMedicalAppointmentDetail(@RequestHeader("Authorization") String authHeader, @RequestParam("userId") Long userId) {
        return iMedicalBookingService.getMedicalAppointmentDetail(authHeader, userId);
    }

    @PostMapping(value = "/changeMedicalAppointment")
    public ResponseEntity<?> changeMedicalAppointment(@RequestBody MedicalBookingDto medicalBookingDto) {
        return iMedicalBookingService.changeMedicalAppointment(medicalBookingDto);
    }

    @GetMapping(value = "/getPreviousSelfDeclaration")
    public ResponseEntity<?> getPreviousSelfDeclaration(@RequestHeader("Authorization") String authHeader, @RequestParam("userId") Long userId) {
        return iMedicalBookingService.getPreviousSelfDeclaration(authHeader, userId);
    }

    @PostMapping(value = "/resubmitSelfDeclaration")
    public ResponseEntity<?> resubmitSelfDeclaration(@RequestBody MedicalBookingDto medicalBookingDto) {
        return iMedicalBookingService.resubmitSelfDeclaration(medicalBookingDto);
    }
}
