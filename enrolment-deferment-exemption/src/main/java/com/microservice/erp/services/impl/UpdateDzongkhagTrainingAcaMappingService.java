package com.microservice.erp.services.impl;

import com.microservice.erp.domain.entities.DzongkhagTrainingPreAcaMapping;
import com.microservice.erp.domain.repositories.IDzongkhagTrainingAcaMappingRepository;
import com.microservice.erp.services.iServices.IUpdateDzongkhagTrainingMappingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdateDzongkhagTrainingAcaMappingService implements IUpdateDzongkhagTrainingMappingService {

    private final IDzongkhagTrainingAcaMappingRepository repository;

    @Override
    public ResponseEntity<?> updateDzongkhagTrainingMapping(DzongkhagTrainingPreAcaMapping dzongkhagTrainingPreAcaMapping) {

        repository.findById(dzongkhagTrainingPreAcaMapping.getId()).ifPresent(d -> {
            d.setDzongkhagId(dzongkhagTrainingPreAcaMapping.getDzongkhagId());
            d.setFirstPreference(dzongkhagTrainingPreAcaMapping.getFirstPreference());
            d.setSecondPreference(dzongkhagTrainingPreAcaMapping.getSecondPreference());
            d.setThirdPreference(dzongkhagTrainingPreAcaMapping.getThirdPreference());
            d.setFourthPreference(dzongkhagTrainingPreAcaMapping.getFourthPreference());
            d.setFifthPreference(dzongkhagTrainingPreAcaMapping.getFifthPreference());
            repository.save(d);
        });

        return ResponseEntity.ok("Dzongkhag and training mapping information updated successfully.");
    }
}
