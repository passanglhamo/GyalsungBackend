package com.microservice.erp.services.impl.services;

import com.microservice.erp.domain.entities.MedicalQuestionnaire;
import com.microservice.erp.domain.repository.IMedicalQuestionnaireRepository;
import com.microservice.erp.services.iServices.IUpdateMedicalQuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdateMedicalQuestionService implements IUpdateMedicalQuestionService {

    private final IMedicalQuestionnaireRepository repository;
    @Override
    public ResponseEntity<?> updateMedicalQuestionnaire(MedicalQuestionnaire medicalQuestionnaire) {
        repository.findById(medicalQuestionnaire.getId()).ifPresent(d -> {
            d.setCategoryId(medicalQuestionnaire.getCategoryId());
            d.setMedicalQuestionName(medicalQuestionnaire.getMedicalQuestionName());
            d.setStatus(medicalQuestionnaire.getStatus());
            repository.save(d);
        });

        return ResponseEntity.ok("Medical Question information updated successfully.");
    }
}
