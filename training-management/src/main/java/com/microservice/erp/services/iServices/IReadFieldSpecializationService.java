package com.microservice.erp.services.iServices;

import com.microservice.erp.domain.entities.FieldSpecialization;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface IReadFieldSpecializationService {
    ResponseEntity<?> findAllByStatus(Character status);

    ResponseEntity<?> findAllMathRequiredCourses();
    ResponseEntity<?> findAllDefaultCourses();

    List<FieldSpecialization> findAll();

}
