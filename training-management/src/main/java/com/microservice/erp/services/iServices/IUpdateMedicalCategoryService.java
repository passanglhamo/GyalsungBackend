package com.microservice.erp.services.iServices;

import com.microservice.erp.domain.entities.MedicalQuestionCategory;
import org.springframework.http.ResponseEntity;

public interface IUpdateMedicalCategoryService {
    ResponseEntity<?> updateMedicalCategories(MedicalQuestionCategory medicalQuestionCategory);

}
