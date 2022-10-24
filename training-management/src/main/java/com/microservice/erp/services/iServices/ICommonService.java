package com.microservice.erp.services.iServices;

import org.springframework.http.ResponseEntity;

public interface ICommonService {
    ResponseEntity<?> getAllDzongkhags();

    ResponseEntity<?> getGeogByDzongkhagId(Integer dzongkhagId);
}
