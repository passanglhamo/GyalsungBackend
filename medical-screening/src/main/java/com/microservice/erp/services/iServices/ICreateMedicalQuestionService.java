package com.microservice.erp.services.iServices;

import com.microservice.erp.domain.entities.MedicalQuestionnaire;

public interface ICreateMedicalQuestionService {
    MedicalQuestionnaire saveMedicalQuestionnaire(MedicalQuestionnaire medicalQuestionnaire);
}
