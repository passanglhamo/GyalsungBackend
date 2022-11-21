package com.microservice.erp.controllers.rest;

import com.microservice.erp.domain.dto.parentConsent.ParentConsentDto;
import com.microservice.erp.services.iServices.parentConsent.IParentConsentService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

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
    public ResponseEntity<?> getParentConsentList(@RequestHeader("Authorization") String authHeader) {
        return parentConsentService.getParentConsentList(authHeader);
    }
}
