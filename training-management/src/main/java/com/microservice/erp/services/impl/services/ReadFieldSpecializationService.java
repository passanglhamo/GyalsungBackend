package com.microservice.erp.services.impl.services;

import com.microservice.erp.domain.entities.FieldSpecialization;
import com.microservice.erp.domain.helper.MessageResponse;
import com.microservice.erp.domain.repository.IFieldSpecializationRepository;
import com.microservice.erp.services.iServices.IReadFieldSpecializationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReadFieldSpecializationService implements IReadFieldSpecializationService {
    private final IFieldSpecializationRepository repository;

    @Override
    public ResponseEntity<?> getAllFieldSpecByStatus(Character status) {
        List<FieldSpecialization> fieldSpecializations = repository.findAllByStatusOrderByFieldSpecNameAsc(status);
        if (fieldSpecializations.size() > 0) {
            return ResponseEntity.ok(fieldSpecializations);
        } else {
            return ResponseEntity.badRequest().body(new MessageResponse("Data not found"));
        }
    }

    @Override
    public ResponseEntity<?> getAllMathRequiredCourses() {
        List<FieldSpecialization> fieldSpecializations = repository.findAllByMathRequiredOrderByFieldSpecNameAsc(true);
        if (fieldSpecializations.size() > 0) {
            return ResponseEntity.ok(fieldSpecializations);
        } else {
            return ResponseEntity.badRequest().body(new MessageResponse("Data not found"));
        }
    }

    @Override
    public ResponseEntity<?> getAllDefaultCourses() {
        List<FieldSpecialization> fieldSpecializations = repository.findAllByDefaultCourseOrderByFieldSpecNameAsc(true);
        if (fieldSpecializations.size() > 0) {
            return ResponseEntity.ok(fieldSpecializations);
        } else {
            return ResponseEntity.badRequest().body(new MessageResponse("Data not found"));
        }
    }

    @Override
    public List<FieldSpecialization> getAllFieldSpecList() {
        return repository.findAllByOrderByFieldSpecNameAsc();
    }

    @Override
    public ResponseEntity<?> getCourseByCourseId(BigInteger courseId) {
        FieldSpecialization fieldSpecialization = repository.findById(courseId).get();
        return ResponseEntity.ok(fieldSpecialization);
    }
}
