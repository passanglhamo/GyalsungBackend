package com.microservice.erp.controllers.rest;

import com.microservice.erp.domain.entities.DzongkhagTrainingPreAcaMapping;
import com.microservice.erp.services.iServices.ICreateDzongkhagTrainingAcaMappingService;
import com.microservice.erp.services.iServices.IReadDzongkhagTrainingAcaMappingService;
import com.microservice.erp.services.iServices.IUpdateDzongkhagTrainingMappingService;
import com.microservice.erp.services.impl.SpringSecurityAuditorAware;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/dzongkhagTrainingMappings")
@AllArgsConstructor
public class DzongkhagTrainingAcaMappingController {

    private final ICreateDzongkhagTrainingAcaMappingService service;
    private final IReadDzongkhagTrainingAcaMappingService readService;
    private final IUpdateDzongkhagTrainingMappingService updateService;

    @PostMapping
    public ResponseEntity<?> saveDzongkhagTraining(@RequestHeader(HttpHeaders.AUTHORIZATION) String token,
                                                   @RequestBody DzongkhagTrainingPreAcaMapping dzongkhagTrainingPreAcaMapping) {
        SpringSecurityAuditorAware.setToken(token);
        return service.saveDzongkhagTraining(dzongkhagTrainingPreAcaMapping);
    }

    @GetMapping
    public List<DzongkhagTrainingPreAcaMapping> getAllDzongkhagTrainingList() {
        return readService.getAllDzongkhagTrainingList();
    }


    @GetMapping(value = "/getAllActiveTrainingsByDzongkhagId", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAllActiveTrainingsByDzongkhagId(@RequestParam("dzongkhagId") Integer dzongkhagId) {
        return readService.getAllActiveTrainingsByDzongkhagId(dzongkhagId);
    }

    @PutMapping("/updateDzongkhagTrainingMapping")
    public ResponseEntity<?> updateDzongkhagTrainingMapping(@RequestHeader(HttpHeaders.AUTHORIZATION) String token,
                                                            @Valid @RequestBody DzongkhagTrainingPreAcaMapping dzongkhagTrainingPreAcaMapping) {
        SpringSecurityAuditorAware.setToken(token);
        return updateService.updateDzongkhagTrainingMapping(dzongkhagTrainingPreAcaMapping);
    }

}
