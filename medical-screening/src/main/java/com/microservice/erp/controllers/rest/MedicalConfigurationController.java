package com.microservice.erp.controllers.rest;

import com.microservice.erp.services.iServices.IMedicalConfigurationService;
import com.microservice.erp.services.impl.services.SpringSecurityAuditorAware;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/medicalConfiguration")
@AllArgsConstructor
public class MedicalConfigurationController {

    private final IMedicalConfigurationService iMedicalConfigurationService;


    @PostMapping(value = "/readFile")
    public ResponseEntity<?> readFile(@RequestHeader("Authorization") String authHeader,@ModelAttribute IMedicalConfigurationService.MedicalExcelCommand command,
                                      @RequestHeader(HttpHeaders.AUTHORIZATION) String token) throws Exception {
        SpringSecurityAuditorAware.setToken(token);
        return iMedicalConfigurationService.readFile(authHeader,command);
    }
}
