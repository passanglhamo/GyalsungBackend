package com.microservice.erp.services.iServices;

import com.microservice.erp.domain.entities.MedicalQuestionnaire;

import java.math.BigInteger;
import java.util.List;

public interface IReadMedicalQuestionService {
    List<MedicalQuestionnaire> getAllMedicalQuestionnaireList();

    MedicalQuestionnaire getAllMedicalQuestionnaireById(BigInteger id);
}
