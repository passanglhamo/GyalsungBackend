package com.microservice.erp.services.iServices.mq;

import com.microservice.erp.domain.entities.MedicalQuestionnaire;

public interface ICreateMedicalQuestionService {
    MedicalQuestionnaire add(MedicalQuestionnaire medicalQuestionnaire);
}
