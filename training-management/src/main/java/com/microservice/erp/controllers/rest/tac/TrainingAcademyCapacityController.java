package com.microservice.erp.controllers.rest.tac;

import com.microservice.erp.domain.dto.es.TrainingAcademyCapacityDto;
import com.microservice.erp.domain.entities.TrainingAcademyCapacity;
import com.microservice.erp.services.iServices.tac.ICreateTrainingAcademyCapacityService;
import com.microservice.erp.services.iServices.tac.IReadTrainingAcademyCapacityService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.text.ParseException;
import java.util.Collection;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/trainingAcademyCapacities")
@AllArgsConstructor
public class TrainingAcademyCapacityController {
    private final ICreateTrainingAcademyCapacityService service;
    private final IReadTrainingAcademyCapacityService readService;

    @PostMapping
    public ResponseEntity<?> save(@RequestBody TrainingAcademyCapacityDto trainingAcademyCapacityDto)
            throws IOException, ParseException {

        return service.save(trainingAcademyCapacityDto);
    }

    @GetMapping
    public Collection<TrainingAcademyCapacity> getAll() {

        return readService.findAll();
    }
}
