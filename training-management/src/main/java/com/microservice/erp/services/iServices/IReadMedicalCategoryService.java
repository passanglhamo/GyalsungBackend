package com.microservice.erp.services.iServices;

import com.microservice.erp.domain.entities.MedicalQuestionCategory;

import java.util.List;

public interface IReadMedicalCategoryService {
    MedicalQuestionCategory getAllMedicalCategoriesById(Long id);

    List<MedicalQuestionCategory> getAllMedicalCategoryList();
    List<MedicalQuestionCategory> getAllActiveMedicalCatList();
}
