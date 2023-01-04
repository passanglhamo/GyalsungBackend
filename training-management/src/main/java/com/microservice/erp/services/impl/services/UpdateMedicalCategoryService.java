package com.microservice.erp.services.impl.services;

import com.microservice.erp.domain.entities.MedicalQuestionCategory;
import com.microservice.erp.domain.repository.IMedicalQuestionCategoryRepository;
import com.microservice.erp.services.iServices.IUpdateMedicalCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdateMedicalCategoryService implements IUpdateMedicalCategoryService {
    private final IMedicalQuestionCategoryRepository repository;

    @Override
    public ResponseEntity<?> updateMedicalCategories(MedicalQuestionCategory medicalQuestionCategory) {
        repository.findById(medicalQuestionCategory.getId()).ifPresent(d -> {
            d.setCategoryName(medicalQuestionCategory.getCategoryName());
            d.setStatus(medicalQuestionCategory.getStatus());
            repository.save(d);
        });

        return ResponseEntity.ok("Medical Category information updated successfully.");
    }
}
