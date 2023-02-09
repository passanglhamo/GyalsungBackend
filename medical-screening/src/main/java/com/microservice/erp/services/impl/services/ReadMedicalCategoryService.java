package com.microservice.erp.services.impl.services;

import com.microservice.erp.domain.entities.MedicalQuestionCategory;
import com.microservice.erp.domain.repositories.IMedicalQuestionCategoryRepository;
import com.microservice.erp.services.iServices.IReadMedicalCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReadMedicalCategoryService implements IReadMedicalCategoryService {

    private final IMedicalQuestionCategoryRepository repository;

    @Override
    public MedicalQuestionCategory getAllMedicalCategoriesById(BigInteger id) {
        return repository.findById(id).get();
    }

    @Override
    public List<MedicalQuestionCategory> getAllMedicalCategoryList() {
        return repository.findAllByOrderByCategoryNameAsc();
    }

    @Override
    public List<MedicalQuestionCategory> getAllActiveMedicalCatList() {
        //todo remove static code
        return repository.findAllByStatusOrderByCategoryNameAsc("A");
    }
}
