package com.microservice.erp.controllers.rest;

import com.microservice.erp.domain.dto.EarlyEnlistmentMedBookingDto;
import com.microservice.erp.services.iServices.IEarlyEnlistmentMedicalBookingService;
import com.microservice.erp.services.impl.services.SpringSecurityAuditorAware;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.math.BigInteger;

@RestController
@RequestMapping("/earlyEnlistmentMedBooking")
@AllArgsConstructor
public class EarlyEnlistmentMedicalBookingController {

    private final IEarlyEnlistmentMedicalBookingService service;

    @PostMapping
    public ResponseEntity<?> save(@RequestHeader("Authorization") String authHeader,
                                  @RequestHeader(HttpHeaders.AUTHORIZATION) String token,
                                  @RequestBody EarlyEnlistmentMedBookingDto earlyEnlistmentMedBookingDto)
            throws IOException {
        SpringSecurityAuditorAware.setToken(token);
        return service.save(authHeader,earlyEnlistmentMedBookingDto);
    }

    @GetMapping("/getEarlyEnlistMedBookingByUserId")
    public ResponseEntity<?> getEarlyEnlistMedBookingByUserId(@RequestParam("userId") BigInteger userId,
                                                                         @RequestParam("earlyEnlistmentId") BigInteger earlyEnlistmentId) {
        return service.getEarlyEnlistMedBookingByUserId(userId,earlyEnlistmentId);
    }
}
