package com.microservice.erp.services.impl.services;

import com.microservice.erp.domain.entities.AgeCriteria;
import com.microservice.erp.domain.helper.MessageResponse;
import com.microservice.erp.domain.repository.IAgeCriteriaRepository;
import com.microservice.erp.services.iServices.IAgeCriteriaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

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
