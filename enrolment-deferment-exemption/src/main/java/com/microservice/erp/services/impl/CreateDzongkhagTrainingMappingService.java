package com.microservice.erp.services.impl;

import com.microservice.erp.domain.entities.DzongkhagTrainingMapping;
import com.microservice.erp.domain.repositories.IDzongkhagTrainingMappingRepository;
import com.microservice.erp.services.iServices.ICreateDzongkhagTrainingMappingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateDzongkhagTrainingMappingService implements ICreateDzongkhagTrainingMappingService {

    private final IDzongkhagTrainingMappingRepository repository;

    @Override
    public ResponseEntity<?> saveDzongkhagTraining(DzongkhagTrainingMapping dzongkhagTrainingMapping) {

        boolean trainingDzongkhagMapExist = repository.existsByTrainingId(dzongkhagTrainingMapping.getTrainingId());

        if (trainingDzongkhagMapExist) {
            return new ResponseEntity<>("Selected training academy is already mapped.", HttpStatus.ALREADY_REPORTED);
        }

        repository.save(dzongkhagTrainingMapping);
        return ResponseEntity.ok("Dzongkhag and training mapped successfully.");
    }
}
