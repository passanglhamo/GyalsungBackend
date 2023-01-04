package com.microservice.erp.services.definition;

import com.microservice.erp.domain.entities.ScreenGroup;
import org.springframework.http.ResponseEntity;

import java.math.BigInteger;

public interface IScreenGroupService {
    ResponseEntity<?> saveScreenGroup(ScreenGroup saScreenGroup);

    ResponseEntity<?> getAllScreens();

    ResponseEntity<?> getScreenById(BigInteger id);

}
