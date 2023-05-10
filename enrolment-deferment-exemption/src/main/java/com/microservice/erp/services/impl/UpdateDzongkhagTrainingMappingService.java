package com.microservice.erp.services.impl;

import com.microservice.erp.domain.entities.DzongkhagTrainingMapping;
import com.microservice.erp.domain.repositories.IDzongkhagTrainingMappingRepository;
import com.microservice.erp.services.iServices.IUpdateDzongkhagTrainingMappingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdateDzongkhagTrainingMappingService implements IUpdateDzongkhagTrainingMappingService {

    private final IDzongkhagTrainingMappingRepository repository;

    @Override
    public ResponseEntity<?> updateDzongkhagTrainingMapping(DzongkhagTrainingMapping dzongkhagTrainingMapping) {

        boolean trainingExistExist = repository.existsByTrainingIdAndIdNot(dzongkhagTrainingMapping.getTrainingId(),
                dzongkhagTrainingMapping.getId());

        if (trainingExistExist) {
            return new ResponseEntity<>("Selected training academy is already mapped.", HttpStatus.ALREADY_REPORTED);
        }

        repository.findById(dzongkhagTrainingMapping.getId()).ifPresent(d -> {
            d.setDzongkhagId(dzongkhagTrainingMapping.getDzongkhagId());
            d.setTrainingId(dzongkhagTrainingMapping.getTrainingId());
            d.setStatus(dzongkhagTrainingMapping.getStatus());
            repository.save(d);
        });

        return ResponseEntity.ok("Dzongkhag and training mapping information updated successfully.");
    }
}
