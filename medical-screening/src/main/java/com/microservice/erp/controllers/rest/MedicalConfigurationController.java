package com.microservice.erp.controllers.rest;

import com.microservice.erp.domain.dto.MedicalConfigurationBulkDto;
import com.microservice.erp.domain.entities.MedicalConfiguration;
import com.microservice.erp.services.iServices.IMedicalConfigurationService;
import com.microservice.erp.services.impl.services.SpringSecurityAuditorAware;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/medicalConfiguration")
@AllArgsConstructor
public class MedicalConfigurationController {

    private final IMedicalConfigurationService iMedicalConfigurationService;


    @PostMapping(value = "/readFile")
    public ResponseEntity<?> readFile(@RequestHeader("Authorization") String authHeader, @ModelAttribute IMedicalConfigurationService.MedicalExcelCommand command,
                                      @RequestHeader(HttpHeaders.AUTHORIZATION) String token) throws Exception {
        SpringSecurityAuditorAware.setToken(token);
        return iMedicalConfigurationService.readFile(authHeader, command);
    }

    @PostMapping(value = "/bulkSave")
    public ResponseEntity<?> bulkSave(@RequestBody MedicalConfigurationBulkDto medicalConfigurationBulkDto,
                                      @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        SpringSecurityAuditorAware.setToken(token);
        return iMedicalConfigurationService.bulkSave(medicalConfigurationBulkDto);
    }

    @GetMapping
    public List<MedicalConfiguration> getAllMedicalConfigurationList() {
        return iMedicalConfigurationService.getAllMedicalConfigurationList();
    }

    @PutMapping("/updateMedicalConfiguration")
    public ResponseEntity<?> updateMedicalConfiguration(@RequestHeader(HttpHeaders.AUTHORIZATION) String token,
                                                        @Valid @RequestBody MedicalConfiguration medicalConfiguration) {
        SpringSecurityAuditorAware.setToken(token);
        return iMedicalConfigurationService.updateMedicalConfiguration(medicalConfiguration);
    }

    @PostMapping
    public MedicalConfiguration save(@RequestHeader(HttpHeaders.AUTHORIZATION) String token,
                                     @Valid @RequestBody MedicalConfiguration medicalConfiguration) {
        SpringSecurityAuditorAware.setToken(token);
        return iMedicalConfigurationService.save(medicalConfiguration);
    }

}
