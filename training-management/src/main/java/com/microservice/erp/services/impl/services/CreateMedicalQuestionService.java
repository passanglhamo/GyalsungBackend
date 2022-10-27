package com.microservice.erp.services.impl.services;

import com.microservice.erp.domain.entities.MedicalQuestionnaire;
import com.microservice.erp.domain.repository.IMedicalQuestionnaireRepository;
import com.microservice.erp.services.iServices.ICreateMedicalQuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateMedicalQuestionService implements ICreateMedicalQuestionService {

    private final IMedicalQuestionnaireRepository repository;

    @Override
    public MedicalQuestionnaire saveMedicalQuestionnaire(MedicalQuestionnaire medicalQuestionnaire) {
        return repository.save(medicalQuestionnaire);
    }
}
