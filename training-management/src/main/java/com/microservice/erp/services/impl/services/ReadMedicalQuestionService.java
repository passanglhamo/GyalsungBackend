package com.microservice.erp.services.impl.services;

import com.microservice.erp.domain.entities.MedicalQuestionnaire;
import com.microservice.erp.domain.repository.IMedicalQuestionnaireRepository;
import com.microservice.erp.services.iServices.IReadMedicalQuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReadMedicalQuestionService implements IReadMedicalQuestionService {
    private final IMedicalQuestionnaireRepository repository;
    @Override
    public List<MedicalQuestionnaire> findAll() {
        return repository.findAll();
    }

    @Override
    public MedicalQuestionnaire findById(Long id) {
        return repository.findById(id).get();
    }
}
