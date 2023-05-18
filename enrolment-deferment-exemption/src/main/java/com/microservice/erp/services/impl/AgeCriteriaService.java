package com.microservice.erp.services.impl;

import com.microservice.erp.domain.entities.AgeCriteria;
import com.microservice.erp.domain.entities.DzongkhagTrainingPreAcaMapping;
import com.microservice.erp.domain.helper.MessageResponse;
import com.microservice.erp.domain.repositories.IAgeCriteriaRepository;
import com.microservice.erp.domain.repositories.IDzongkhagTrainingAcaMappingRepository;
import com.microservice.erp.services.iServices.IAgeCriteriaService;
import com.microservice.erp.services.iServices.ICreateDzongkhagTrainingAcaMappingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AgeCriteriaService implements IAgeCriteriaService {
    private final IAgeCriteriaRepository iAgeCriteriaRepository;

    @Override
    public ResponseEntity<?> saveAgeCriteria(AgeCriteria ageCriteria) {
        iAgeCriteriaRepository.deleteAll();
        iAgeCriteriaRepository.save(ageCriteria);
        return ResponseEntity.ok(new MessageResponse("Data saved successfully."));
    }

    @Override
    public ResponseEntity<?> getAgeCriteria() {
        AgeCriteria ageCriteria = iAgeCriteriaRepository.findTopByOrderByMinimumAgeDesc();
        if (ageCriteria != null) {
            return ResponseEntity.ok(ageCriteria);
        } else {
            return ResponseEntity.badRequest().body(new MessageResponse("Data not found."));
        }
    }
}
