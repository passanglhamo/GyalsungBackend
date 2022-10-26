package com.microservice.erp.services.iServices;

import com.microservice.erp.domain.entities.MedicalQuestionCategory;

public interface ICreateMedicalCategoryService {
    MedicalQuestionCategory add(MedicalQuestionCategory medicalQuestionCategory);
}
