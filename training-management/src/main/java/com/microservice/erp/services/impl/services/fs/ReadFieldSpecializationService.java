package com.microservice.erp.services.impl.services.fs;

import com.microservice.erp.domain.entities.FieldSpecialization;
import com.microservice.erp.domain.helper.MessageResponse;
import com.microservice.erp.domain.repository.IFieldSpecializationRepository;
import com.microservice.erp.services.iServices.fs.IReadFieldSpecializationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ReadFieldSpecializationService implements IReadFieldSpecializationService {
    private final IFieldSpecializationRepository repository;

    @Override
    public ResponseEntity<?> findAllByStatus(Character status) {
        List<FieldSpecialization> fieldSpecializations = repository.findAllByStatusOrderByNameAsc(status);
        if (fieldSpecializations.size() > 0) {
            return ResponseEntity.ok(fieldSpecializations);
        } else {
            return ResponseEntity.badRequest().body(new MessageResponse("Data not found"));
        }
    }

    @Override
    public ResponseEntity<?> findAllMathRequiredCourses() {
        List<FieldSpecialization> fieldSpecializations = repository.findAllByMathRequiredOrderByNameAsc(true);
        if (fieldSpecializations.size() > 0) {
            return ResponseEntity.ok(fieldSpecializations);
        } else {
            return ResponseEntity.badRequest().body(new MessageResponse("Data not found"));
        }
    }  @Override
    public ResponseEntity<?> findAllDefaultCourses() {
        List<FieldSpecialization> fieldSpecializations = repository.findAllByDefaultCourseOrderByNameAsc(true);
        if (fieldSpecializations.size() > 0) {
            return ResponseEntity.ok(fieldSpecializations);
        } else {
            return ResponseEntity.badRequest().body(new MessageResponse("Data not found"));
        }
    }

    @Override
    public List<FieldSpecialization> findAll() {
        return repository.findAll();
    }
}
