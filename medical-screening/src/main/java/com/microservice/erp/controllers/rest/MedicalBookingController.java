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
}
