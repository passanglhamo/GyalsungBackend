package com.microservice.erp.services.impl.services;

import com.microservice.erp.domain.entities.FieldSpecialization;
import com.microservice.erp.domain.repository.IFieldSpecializationRepository;
import com.microservice.erp.services.iServices.ICreateFieldSpecializationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateFieldSpecializationService implements ICreateFieldSpecializationService {

    private final IFieldSpecializationRepository repository;

    @Override
    public FieldSpecialization saveFieldSpec(FieldSpecialization fieldSpecialization) {
        return repository.save(fieldSpecialization);
    }
}
