package com.microservice.erp.controllers.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.microservice.erp.domain.dto.DefermentListDto;
import com.microservice.erp.domain.dto.EarlyEnlistmentDto;
import com.microservice.erp.domain.dto.GuardianConsentRequestDto;
import com.microservice.erp.services.iServices.IEarlyEnlistmentService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.List;


@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/earlyEnlistment")
@AllArgsConstructor
public class EarlyEnlistmentController {

    private final IEarlyEnlistmentService iEarlyEnlistmentService;

    @RequestMapping(value = "/checkAgeValidation", method = RequestMethod.POST)
    public ResponseEntity<?> checkAgeValidation(@RequestHeader("Authorization") String authHeader
            , @RequestBody EarlyEnlistmentDto earlyEnlistmentDto)  {
        return iEarlyEnlistmentService.checkAgeValidation(authHeader, earlyEnlistmentDto);
    }

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
    public ResponseEntity<?> applyEarlyEnlistment(@RequestHeader("Authorization") String authHeader,
                                                  @RequestBody EarlyEnlistmentDto earlyEnlistmentDto) throws JsonProcessingException {
        return iEarlyEnlistmentService.applyEarlyEnlistment(authHeader, earlyEnlistmentDto);
    }

    @RequestMapping(value = "/getEarlyEnlistmentStatus", method = RequestMethod.GET)
    public ResponseEntity<?> getEarlyEnlistmentStatus(@RequestParam("userId") BigInteger userId) {
        return iEarlyEnlistmentService.getEarlyEnlistmentStatus(userId);
    }

    @GetMapping(value = "/getEarlyEnlistmentListByCriteria")
    public List<DefermentListDto> getEarlyEnlistmentListByCriteria(@RequestHeader("Authorization") String authHeader
            , @RequestParam("status") Character status
            , @RequestParam("gender") Character gender
            , @RequestParam("cid") String cid) {
        return iEarlyEnlistmentService.getEarlyEnlistmentListByCriteria(authHeader, status, gender, cid);
    }


}
