package com.microservice.erp.services.impl.services.mq;

import com.microservice.erp.domain.entities.MedicalQuestionnaire;
import com.microservice.erp.domain.repository.IMedicalQuestionnaireRepository;
import com.microservice.erp.services.iServices.mq.ICreateMedicalQuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateMedicalQuestionService implements ICreateMedicalQuestionService {

    private final IMedicalQuestionnaireRepository repository;

    @Override
    public MedicalQuestionnaire add(MedicalQuestionnaire medicalQuestionnaire) {
        return repository.save(medicalQuestionnaire);
    }
}
