package com.microservice.erp.controllers.rest;

import com.microservice.erp.domain.dto.HospitalBookingDetailsDto;
import com.microservice.erp.services.iServices.IHospitalBookingDetailsService;
import com.microservice.erp.services.impl.services.SpringSecurityAuditorAware;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;

@RestController
@RequestMapping("/hospitalBookingDetails")
@AllArgsConstructor
public class HospitalBookingDetailsController {
    private final IHospitalBookingDetailsService hospitalBookingDetailsService;

    @PostMapping(value = "/preBookHospitalAppointment")
    public ResponseEntity<?> preBookHospitalAppointment(@RequestHeader(HttpHeaders.AUTHORIZATION) String token,
                                                        @RequestHeader("Authorization") String authHeader,
                                                        @RequestBody HospitalBookingDetailsDto hospitalBookingDetailsDto) throws Exception {
        SpringSecurityAuditorAware.setToken(token);
        return hospitalBookingDetailsService.preBookHospitalAppointment(authHeader, hospitalBookingDetailsDto);
    }

    @GetMapping(value = "/getHospitalBookingDetailByUserId")
    public HospitalBookingDetailsDto getHospitalBookingDetailByUserId(@RequestHeader("Authorization") String authHeader,
                                                                      @RequestParam("userId") BigInteger userId) {
        return hospitalBookingDetailsService.getHospitalBookingDetailByUserId(authHeader, userId);
    }

    @PostMapping(value = "/bookHospitalAppointment")
    public ResponseEntity<?> bookHospitalAppointment(@RequestHeader(HttpHeaders.AUTHORIZATION) String token,
                                                     @RequestHeader("Authorization") String authHeader,
                                                     @RequestBody IHospitalBookingDetailsService.BookHospitalCommand command) throws Exception {
        SpringSecurityAuditorAware.setToken(token);
        return hospitalBookingDetailsService.bookHospitalAppointment(authHeader, command,'S');
    }

}
