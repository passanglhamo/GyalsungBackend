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

    @PostMapping(value = "/getMedicalAppointmentDetail")
    public ResponseEntity<?> getMedicalAppointmentDetail(@RequestParam("userId") Long userId) {
        return iMedicalBookingService.getMedicalAppointmentDetail(userId);
    }

    @PostMapping(value = "/editMedicalAppointment")
    public ResponseEntity<?> editMedicalAppointment(@RequestBody MedicalBookingDto medicalBookingDto) {
        return iMedicalBookingService.editMedicalAppointment(medicalBookingDto);
    }

}
