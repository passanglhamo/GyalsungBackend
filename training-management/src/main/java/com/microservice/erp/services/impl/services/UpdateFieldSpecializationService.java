package com.microservice.erp.services.impl.services;

import com.microservice.erp.domain.entities.FieldSpecialization;
import com.microservice.erp.domain.repository.IFieldSpecializationRepository;
import com.microservice.erp.services.iServices.IUpdateFieldSpecializationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdateFieldSpecializationService implements IUpdateFieldSpecializationService {

    private final IFieldSpecializationRepository repository;

    @Override
    public ResponseEntity<?> updateCourse(FieldSpecialization fieldSpecialization) {
        repository.findById(fieldSpecialization.getId()).ifPresent(d -> {
            d.setFieldSpecName(fieldSpecialization.getFieldSpecName());
            d.setDefaultCourse(fieldSpecialization.getDefaultCourse());
            d.setMathRequired(fieldSpecialization.getMathRequired());
            d.setStatus(fieldSpecialization.getStatus());
            repository.save(d);
        });

        return ResponseEntity.ok("Course information updated successfully.");
    }
}
