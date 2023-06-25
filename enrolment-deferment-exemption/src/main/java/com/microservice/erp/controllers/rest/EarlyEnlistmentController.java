package com.microservice.erp.controllers.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.microservice.erp.domain.dto.GuardianConsentRequestDto;
import com.microservice.erp.services.iServices.IEarlyEnlistmentService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;


@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/earlyEnlistment")
@AllArgsConstructor
public class EarlyEnlistmentController {

    private final IEarlyEnlistmentService iEarlyEnlistmentService;

    @RequestMapping(value = "/requestGuardianConsent", method = RequestMethod.POST)
    public ResponseEntity<?> requestGuardianConsent(@RequestHeader("Authorization") String authHeader
            , @RequestBody GuardianConsentRequestDto guardianConsentRequestDto) throws JsonProcessingException {
        return iEarlyEnlistmentService.requestGuardianConsent(authHeader, guardianConsentRequestDto);
    }

    @RequestMapping(value = "/getGuardianConsentStatus", method = RequestMethod.GET)
    public ResponseEntity<?> getGuardianConsentStatus(@RequestParam("userId") BigInteger userId) {
        return iEarlyEnlistmentService.getGuardianConsentStatus(userId);
    }

    @RequestMapping(value = "/applyEarlyEnlistment", method = RequestMethod.POST)
    public ResponseEntity<?> applyEarlyEnlistment(@RequestHeader("Authorization") String authHeader, @RequestParam("userId") BigInteger userId) throws JsonProcessingException {
        return iEarlyEnlistmentService.applyEarlyEnlistment(authHeader, userId);
    }

}
