package com.microservice.erp.services.iServices;

import org.springframework.http.ResponseEntity;

public interface IDashBoardService {
    ResponseEntity<?> getEdeFigure(String year);

    ResponseEntity<?> getTotalRegisteredList(String authHeader, String year);

    ResponseEntity<?> getEarlyEnlistmentList(String authHeader, String year);

    ResponseEntity<?> getDeferredList(String authHeader, String year);

    ResponseEntity<?> getExemptedList(String authHeader, String year);

    ResponseEntity<?> getAcademyWiseEnrolmentFigure(String authHeader, String year);

    ResponseEntity<?> getTaskStatusByYear(String authHeader, String year);
}
