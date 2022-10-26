package com.microservice.erp.services.iServices;

import com.microservice.erp.domain.entities.MedicalQuestionnaire;

import java.util.List;

public interface IReadMedicalQuestionService {
    List<MedicalQuestionnaire> getMedicalQuestionnaireList();

    MedicalQuestionnaire findById(Long id);
}
