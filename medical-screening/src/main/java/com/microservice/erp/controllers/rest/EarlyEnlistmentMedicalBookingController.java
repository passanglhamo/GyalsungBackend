package com.microservice.erp.controllers.rest;

import com.infoworks.lab.jjwt.JWTPayload;
import com.infoworks.lab.jjwt.TokenValidator;
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

    @RequestMapping(value = "/bookMedicalAppointment", method = RequestMethod.POST)
    public ResponseEntity<?> bookMedicalAppointment(@RequestHeader("Authorization") String authHeader,
                                                    @RequestHeader(HttpHeaders.AUTHORIZATION) String token,
                                                    @RequestBody EarlyEnlistmentMedBookingDto earlyEnlistmentMedBookingDto) throws Exception {
        JWTPayload jwtPayload = TokenValidator.parsePayload(token, JWTPayload.class);
        BigInteger currentUserId = new BigInteger(jwtPayload.getSub());
        return service.bookMedicalAppointment(authHeader, currentUserId, earlyEnlistmentMedBookingDto);
    }

    @GetMapping("/getEarlyEnlistMedBookingById")
    public ResponseEntity<?> getEarlyEnlistMedBookingById(@RequestParam("earlyEnlistmentId") BigInteger earlyEnlistmentId) {
        return service.getEarlyEnlistMedBookingById(earlyEnlistmentId);
    }
}
