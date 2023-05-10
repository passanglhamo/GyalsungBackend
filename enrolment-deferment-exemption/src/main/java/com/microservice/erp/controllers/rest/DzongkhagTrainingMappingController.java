package com.microservice.erp.controllers.rest;

import com.microservice.erp.domain.entities.DzongkhagTrainingMapping;
import com.microservice.erp.services.iServices.ICreateDzongkhagTrainingMappingService;
import com.microservice.erp.services.iServices.IReadDzongkhagTrainingMappingService;
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
public class DzongkhagTrainingMappingController {

    private final ICreateDzongkhagTrainingMappingService service;
    private final IReadDzongkhagTrainingMappingService readService;
    private final IUpdateDzongkhagTrainingMappingService updateService;

    @PostMapping
    public ResponseEntity<?> saveDzongkhagTraining(@RequestHeader(HttpHeaders.AUTHORIZATION) String token,
                                                   @Valid @RequestBody DzongkhagTrainingMapping dzongkhagTrainingMapping) {
        SpringSecurityAuditorAware.setToken(token);
        return service.saveDzongkhagTraining(dzongkhagTrainingMapping);
    }

    @GetMapping
    public List<DzongkhagTrainingMapping> getAllDzongkhagTrainingList() {
        return readService.getAllDzongkhagTrainingList();
    }

    @GetMapping("/getAllDzongkhagTrainingByStatus")
    public List<DzongkhagTrainingMapping> getAllDzongkhagTrainingByStatus(@RequestParam("status") String status) {
        return readService.getAllDzongkhagTrainingByStatus(status);
    }

    @GetMapping(value = "/getAllActiveTrainingsByDzongkhagId", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAllActiveTrainingsByDzongkhagId(@RequestParam("dzongkhagId") Integer dzongkhagId) {
        return readService.getAllActiveTrainingsByDzongkhagId(dzongkhagId);
    }

    @PutMapping("/updateDzongkhagTrainingMapping")
    public ResponseEntity<?> updateDzongkhagTrainingMapping(@RequestHeader(HttpHeaders.AUTHORIZATION) String token,
                                                            @Valid @RequestBody DzongkhagTrainingMapping dzongkhagTrainingMapping) {
        SpringSecurityAuditorAware.setToken(token);
        return updateService.updateDzongkhagTrainingMapping(dzongkhagTrainingMapping);
    }

}
