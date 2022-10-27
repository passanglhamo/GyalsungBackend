package com.microservice.erp.services.impl.services;

import com.microservice.erp.domain.entities.MedicalQuestionCategory;
import com.microservice.erp.domain.helper.Status;
import com.microservice.erp.domain.repository.IMedicalQuestionCategoryRepository;
import com.microservice.erp.services.iServices.IReadMedicalCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReadMedicalCategoryService implements IReadMedicalCategoryService {

    private final IMedicalQuestionCategoryRepository repository;

    @Override
    public MedicalQuestionCategory getAllMedicalCategoriesById(Long id) {
        return repository.findById(id).get();
    }

    @Override
    public List<MedicalQuestionCategory> getAllMedicalCategoryList() {
        return repository.findAll();
    }

    @Override
    public List<MedicalQuestionCategory> getAllActiveMedicalCatList() {
        return repository.findAllByStatus(Status.Active.value().toString());
    }
}
