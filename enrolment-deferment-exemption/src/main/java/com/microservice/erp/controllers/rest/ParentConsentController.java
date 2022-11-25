package com.microservice.erp.controllers.rest;

import com.microservice.erp.domain.dto.ParentConsentDto;
import com.microservice.erp.services.iServices.IParentConsentService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/parentConsent")
@AllArgsConstructor
public class ParentConsentController {
    private IParentConsentService parentConsentService;

    @PostMapping(value = "/receiveOtp")
    public ResponseEntity<?> receiveOtp(@RequestBody ParentConsentDto parentConsentDto) throws Exception {
        return parentConsentService.receiveOtp(parentConsentDto);
    }

    @PostMapping(value = "/submitParentConsent")
    public ResponseEntity<?> submitParentConsent(@RequestBody ParentConsentDto parentConsentDto) throws Exception {
        return parentConsentService.submitParentConsent(parentConsentDto);
    }

    @GetMapping(value = "/getParentConsentList")
    public ResponseEntity<?> getParentConsentList(@RequestHeader("Authorization") String authHeader
            , @RequestParam("year") String year
            , @RequestParam("status") Character status) {
        return parentConsentService.getParentConsentList(authHeader, year, status);
    }
}
