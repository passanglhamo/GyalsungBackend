package com.microservice.erp.services.iServices;

import com.microservice.erp.domain.entities.MedicalQuestionnaire;
import org.springframework.http.ResponseEntity;

public interface IUpdateMedicalQuestionService {

    ResponseEntity<?> updateMedicalQuestionnaire(MedicalQuestionnaire medicalQuestionnaire);
}
