package com.microservice.erp.services.iServices.mc;

import com.microservice.erp.domain.entities.MedicalQuestionCategory;

import java.util.List;

public interface IReadMedicalCategoryService {
    MedicalQuestionCategory findById(Long id);

    List<MedicalQuestionCategory> findAll();
}
