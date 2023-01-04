package com.microservice.erp.controllers.rest;

import com.microservice.erp.domain.dto.TrainingAcademyCapacityDto;
import com.microservice.erp.domain.entities.TrainingAcademyCapacity;
import com.microservice.erp.services.iServices.ICreateTrainingAcademyCapacityService;
import com.microservice.erp.services.iServices.IReadTrainingAcademyCapacityService;
import com.microservice.erp.services.iServices.IUpdateTrainingAcademyIntakeService;
import com.microservice.erp.services.impl.services.SpringSecurityAuditorAware;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.math.BigInteger;
import java.text.ParseException;
import java.util.Collection;

@RestController
@RequestMapping("/trainingAcademyCapacities")
@AllArgsConstructor
public class TrainingAcademyCapacityController {
    private final ICreateTrainingAcademyCapacityService service;
    private final IReadTrainingAcademyCapacityService readService;
    private final IUpdateTrainingAcademyIntakeService updateService;

    @PostMapping
    public ResponseEntity<?> saveTrainingAcaCap(@RequestHeader(HttpHeaders.AUTHORIZATION) String token,
                                                @Valid @RequestBody TrainingAcademyCapacityDto trainingAcademyCapacityDto)
            throws IOException, ParseException {
        SpringSecurityAuditorAware.setToken(token);
        return service.saveTrainingAcaCap(trainingAcademyCapacityDto);
    }

    @GetMapping
    public Collection<TrainingAcademyCapacity> getAllTrainingAcaCapList() {

        return readService.getAllTrainingAcaCapList();
    }

    @GetMapping("/getAllTrainingAcaCapById")
    public TrainingAcademyCapacity getAllTrainingAcaCapById(@RequestParam("id") BigInteger id) {
        return readService.getAllTrainingAcaCapById(id);
    }

    @PutMapping("/updateTrainingAcademyIntake")
    public ResponseEntity<?> updateTrainingAcademyIntake(@RequestHeader(HttpHeaders.AUTHORIZATION) String token,
                                                         @Valid @RequestBody TrainingAcademyCapacityDto trainingAcademyCapacityDto) {
        SpringSecurityAuditorAware.setToken(token);
        return updateService.updateTrainingAcademyIntake(trainingAcademyCapacityDto);
    }
}
