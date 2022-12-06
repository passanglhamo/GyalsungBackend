package com.microservice.erp.services.iServices;

import com.microservice.erp.domain.entities.RegistrationDateInfo;
import org.springframework.http.ResponseEntity;

public interface IUpdateRegistrationDateInfoService {
    ResponseEntity<?> updateRegistrationDateInfo(RegistrationDateInfo registrationDateInfo);
}
