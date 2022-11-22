package com.microservice.erp.services.iServices;

import com.microservice.erp.domain.entities.MedicalQuestionCategory;

import java.math.BigInteger;
import java.util.List;

public interface IReadMedicalCategoryService {
    MedicalQuestionCategory getAllMedicalCategoriesById(BigInteger id);

    List<MedicalQuestionCategory> getAllMedicalCategoryList();
    List<MedicalQuestionCategory> getAllActiveMedicalCatList();
}
