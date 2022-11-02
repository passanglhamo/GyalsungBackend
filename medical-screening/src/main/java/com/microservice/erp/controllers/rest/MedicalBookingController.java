package com.microservice.erp.controllers.rest;

import com.microservice.erp.domain.dto.MedicalBookingDto;
import com.microservice.erp.services.iServices.IMedicalBookingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/medicalBooking")
public class MedicalBookingController {
    private IMedicalBookingService iMedicalBookingService;

    @PostMapping(value = "/testApi")
    public ResponseEntity<?> testApi() {
        return ResponseEntity.ok("Hello tester");
    }

    @PostMapping(value = "/bookMedicalAppointment")
    public ResponseEntity<?> bookMedicalAppointment(@RequestBody MedicalBookingDto medicalBookingDto) {
        return null;
    }
}
