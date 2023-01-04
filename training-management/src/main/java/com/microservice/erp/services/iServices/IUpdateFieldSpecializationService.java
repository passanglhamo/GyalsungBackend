package com.microservice.erp.services.iServices;

import com.microservice.erp.domain.entities.FieldSpecialization;
import org.springframework.http.ResponseEntity;

public interface IUpdateFieldSpecializationService {

    ResponseEntity<?> updateCourse(FieldSpecialization fieldSpecialization);
}
