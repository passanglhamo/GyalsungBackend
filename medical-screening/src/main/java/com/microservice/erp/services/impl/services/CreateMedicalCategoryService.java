package com.microservice.erp.services.impl.services;

import com.microservice.erp.domain.entities.MedicalQuestionCategory;
import com.microservice.erp.domain.repositories.IMedicalQuestionCategoryRepository;
import com.microservice.erp.services.iServices.ICreateMedicalCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateMedicalCategoryService implements ICreateMedicalCategoryService {

    private final IMedicalQuestionCategoryRepository repository;

    @Override
    public MedicalQuestionCategory saveMedicalCategory(MedicalQuestionCategory medicalQuestionCategory) {
        return repository.save(medicalQuestionCategory);
    }

}
