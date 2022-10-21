package com.microservice.erp.services.iServices.mc;

import com.microservice.erp.domain.entities.MedicalQuestionCategory;

public interface ICreateMedicalCategoryService {
    MedicalQuestionCategory add(MedicalQuestionCategory medicalQuestionCategory);
}
