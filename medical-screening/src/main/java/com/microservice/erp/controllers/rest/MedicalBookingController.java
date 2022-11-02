package com.microservice.erp.controllers.rest;

import com.microservice.erp.domain.dto.MedicalBookingDto;
import com.microservice.erp.services.iServices.IMedicalBookingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/medicalBooking")
public class MedicalBookingController {
    private IMedicalBookingService iMedicalBookingService;

    @GetMapping(value = "/bookMedicalAppointment")
//    public ResponseEntity<?> bookMedicalAppointment( MedicalBookingDto medicalBookingDto) {
    public ResponseEntity<?> bookMedicalAppointment( ) {
//    public ResponseEntity<?> bookMedicalAppointment(@ModelAttribute MedicalDto medicalDto) {
        return null;
    }
}
