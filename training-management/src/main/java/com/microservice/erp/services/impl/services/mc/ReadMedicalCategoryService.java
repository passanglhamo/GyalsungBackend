package com.microservice.erp.services.impl.services.mc;

import com.microservice.erp.domain.entities.MedicalQuestionCategory;
import com.microservice.erp.domain.repository.IMedicalQuestionCategoryRepository;
import com.microservice.erp.services.iServices.mc.IReadMedicalCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReadMedicalCategoryService implements IReadMedicalCategoryService {

    private final IMedicalQuestionCategoryRepository repository;

    @Override
    public MedicalQuestionCategory findById(Long id) {
        return repository.findById(id).get();
    }

    @Override
    public List<MedicalQuestionCategory> findAll() {
        return repository.findAll();
    }
}
