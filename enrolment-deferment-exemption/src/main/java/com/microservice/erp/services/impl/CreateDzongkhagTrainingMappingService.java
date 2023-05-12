package com.microservice.erp.services.impl;

import com.microservice.erp.domain.entities.DzongkhagTrainingPreAcaMapping;
import com.microservice.erp.domain.repositories.IDzongkhagTrainingAcaMappingRepository;
import com.microservice.erp.services.iServices.ICreateDzongkhagTrainingAcaMappingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateDzongkhagTrainingMappingService implements ICreateDzongkhagTrainingAcaMappingService {

    private final IDzongkhagTrainingAcaMappingRepository repository;

    @Override
    public ResponseEntity<?> saveDzongkhagTraining(DzongkhagTrainingPreAcaMapping dzongkhagTrainingPreAcaMapping) {
        repository.save(dzongkhagTrainingPreAcaMapping);
        return ResponseEntity.ok("Dzongkhag and training mapped successfully.");
    }
}
