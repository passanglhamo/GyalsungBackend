package com.microservice.erp.controllers.rest;

import com.microservice.erp.domain.dto.NSRegistrationDto;
import com.microservice.erp.services.iServices.INSRegistrationService;
import com.microservice.erp.services.impl.SpringSecurityAuditorAware;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/nsregistration")
@AllArgsConstructor
public class NSRegistrationController {

    private final INSRegistrationService insRegistrationService;

    @PostMapping
    public ResponseEntity<?> save(@RequestHeader("Authorization") String authHeader,
                                           @RequestHeader(HttpHeaders.AUTHORIZATION) String token,
                                           @RequestBody NSRegistrationDto nsRegistrationDto) throws Exception {
        SpringSecurityAuditorAware.setToken(token);
        return insRegistrationService.save(authHeader, nsRegistrationDto);
    }

    @RequestMapping(value = "/getMyRegistrationInfo", method = RequestMethod.GET)
    public ResponseEntity<?> getMyRegistrationInfo(@RequestParam("userId") BigInteger userId) {
        return insRegistrationService.getMyRegistrationInfo(userId);
    }

}
