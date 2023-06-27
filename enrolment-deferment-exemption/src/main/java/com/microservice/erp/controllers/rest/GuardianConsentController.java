package com.microservice.erp.controllers.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.microservice.erp.domain.dto.GuardianConsentDto;
import com.microservice.erp.services.iServices.IGuardianConsentService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/guardianConsent")
@AllArgsConstructor
public class GuardianConsentController {

    private final IGuardianConsentService iGuardianConsentService;

    @RequestMapping(value = "/validateGuardianConsentLink", method = RequestMethod.POST)
    public ResponseEntity<?> validateGuardianConsentLink(@RequestBody GuardianConsentDto guardianConsentDto) {
        return iGuardianConsentService.validateGuardianConsentLink(guardianConsentDto);
    }

    @RequestMapping(value = "/validateGuardian", method = RequestMethod.POST)
    public ResponseEntity<?> validateGuardian(@RequestBody GuardianConsentDto guardianConsentDto) {
        return iGuardianConsentService.validateGuardian(guardianConsentDto);
    }

    @RequestMapping(value = "/receiveOtp", method = RequestMethod.POST)
    public ResponseEntity<?> receiveOtp(@RequestBody GuardianConsentDto guardianConsentDto) throws JsonProcessingException {
        return iGuardianConsentService.receiveOtp(guardianConsentDto);
    }

    @RequestMapping(value = "/verifyOtp", method = RequestMethod.POST)
    public ResponseEntity<?> verifyOtp(@RequestBody GuardianConsentDto guardianConsentDto) {
        return iGuardianConsentService.verifyOtp(guardianConsentDto);
    }

}
