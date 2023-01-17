package com.microservice.erp.controllers.rest;

import com.microservice.erp.domain.dto.MedicalBookingDto;
import com.microservice.erp.services.iServices.IMedicalBookingService;
import com.microservice.erp.services.impl.services.SpringSecurityAuditorAware;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;

@RestController
@AllArgsConstructor
@RequestMapping("/medicalBooking")
public class MedicalBookingController {
    private IMedicalBookingService iMedicalBookingService;

    @PostMapping(value = "/bookMedicalAppointment")
    public ResponseEntity<?> bookMedicalAppointment(@RequestHeader(HttpHeaders.AUTHORIZATION) String token,
                                                    @RequestHeader("Authorization") String authHeader,
                                                    @RequestBody MedicalBookingDto medicalBookingDto) throws Exception {
        SpringSecurityAuditorAware.setToken(token);
        return iMedicalBookingService.bookMedicalAppointment(authHeader, medicalBookingDto);
    }

    @GetMapping(value = "/getMedicalAppointmentDetail")
    public ResponseEntity<?> getMedicalAppointmentDetail(@RequestHeader("Authorization") String authHeader, @RequestParam("userId") BigInteger userId) {
        return iMedicalBookingService.getMedicalAppointmentDetail(authHeader, userId);
    }

    @PostMapping(value = "/changeMedicalAppointment")
    public ResponseEntity<?> changeMedicalAppointment(@RequestHeader(HttpHeaders.AUTHORIZATION) String token,
                                                      @RequestHeader("Authorization") String authHeader,
                                                      @RequestBody MedicalBookingDto medicalBookingDto) throws Exception {
        SpringSecurityAuditorAware.setToken(token);
        return iMedicalBookingService.changeMedicalAppointment(authHeader, medicalBookingDto);
    }

    @GetMapping(value = "/getPreviousSelfDeclaration")
    public ResponseEntity<?> getPreviousSelfDeclaration(@RequestHeader("Authorization") String authHeader, @RequestParam("userId") BigInteger userId) {
        return iMedicalBookingService.getPreviousSelfDeclaration(authHeader, userId);
    }

    @PostMapping(value = "/resubmitSelfDeclaration")
    public ResponseEntity<?> resubmitSelfDeclaration(@RequestHeader(HttpHeaders.AUTHORIZATION) String token,
                                                     @RequestBody MedicalBookingDto medicalBookingDto) {
        SpringSecurityAuditorAware.setToken(token);
        return iMedicalBookingService.resubmitSelfDeclaration(medicalBookingDto);
    }
}
