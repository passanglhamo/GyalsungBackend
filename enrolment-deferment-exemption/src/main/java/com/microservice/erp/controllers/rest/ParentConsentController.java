package com.microservice.erp.controllers.rest;

import com.microservice.erp.domain.dto.ParentConsentDto;
import com.microservice.erp.services.iServices.IParentConsentService;
import com.microservice.erp.services.impl.SpringSecurityAuditorAware;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/parentConsent")
@AllArgsConstructor
public class ParentConsentController {
    private IParentConsentService parentConsentService;

    @PostMapping(value = "/receiveOtp")
    public ResponseEntity<?> receiveOtp(@RequestHeader(HttpHeaders.AUTHORIZATION) String token,
                                        @RequestBody ParentConsentDto parentConsentDto) throws Exception {
        SpringSecurityAuditorAware.setToken(token);
        return parentConsentService.receiveOtp(parentConsentDto);
    }

    @PostMapping(value = "/submitParentConsent")
    public ResponseEntity<?> submitParentConsent( @RequestHeader(HttpHeaders.AUTHORIZATION) String token,
                                                  @RequestBody ParentConsentDto parentConsentDto) throws Exception {
        SpringSecurityAuditorAware.setToken(token);
        return parentConsentService.submitParentConsent(parentConsentDto);
    }

    @GetMapping(value = "/getParentConsentList")
    public ResponseEntity<?> getParentConsentList(@RequestHeader("Authorization") String authHeader
            , @RequestParam("year") String year
            , @RequestParam("status") Character status) {
        return parentConsentService.getParentConsentList(authHeader, year, status);
    }
}
