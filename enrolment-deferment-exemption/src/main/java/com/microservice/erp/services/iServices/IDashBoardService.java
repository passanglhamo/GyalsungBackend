package com.microservice.erp.services.iServices;

import org.springframework.http.ResponseEntity;

public interface IDashBoardService {
    ResponseEntity<?> getRegistrationInfoFigures(String year);
}
