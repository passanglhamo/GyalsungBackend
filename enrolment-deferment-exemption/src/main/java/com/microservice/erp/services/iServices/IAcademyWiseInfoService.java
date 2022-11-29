package com.microservice.erp.services.iServices;

import org.springframework.http.ResponseEntity;

public interface IAcademyWiseInfoService {
    ResponseEntity<?> getEnrolmentFigureByYear(String authHeader, String year);
}
