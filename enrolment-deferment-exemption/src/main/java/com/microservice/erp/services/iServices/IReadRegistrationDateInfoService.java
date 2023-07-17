package com.microservice.erp.services.iServices;

import com.microservice.erp.domain.entities.RegistrationDateInfo;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface IReadRegistrationDateInfoService {
    List<RegistrationDateInfo> getAllRegistrationDateList();

    ResponseEntity<?> getRegistrationDateInfo();
}
