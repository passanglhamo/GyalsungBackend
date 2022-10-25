package com.microservice.erp.services.iServices.mq;

import com.microservice.erp.domain.entities.MedicalQuestionnaire;

import java.util.List;

public interface IReadMedicalQuestionService {
    List<MedicalQuestionnaire> findAll();

    MedicalQuestionnaire findById(Long id);
}
