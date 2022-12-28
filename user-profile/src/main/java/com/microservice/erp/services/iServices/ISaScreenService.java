package com.microservice.erp.services.iServices;

import com.microservice.erp.domain.entities.SaScreen;
import org.springframework.http.ResponseEntity;

import java.math.BigInteger;

public interface ISaScreenService {
    ResponseEntity<?> saveScreen(SaScreen saScreen);

    ResponseEntity<?> getAllScreens();

    ResponseEntity<?> getScreenById(BigInteger id);
}
