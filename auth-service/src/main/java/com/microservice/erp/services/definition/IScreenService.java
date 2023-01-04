package com.microservice.erp.services.definition;

import com.microservice.erp.domain.entities.Screen;
import org.springframework.http.ResponseEntity;

import java.math.BigInteger;

public interface IScreenService {
    ResponseEntity<?> saveScreen(Screen saScreen);

    ResponseEntity<?> getAllScreens();

    ResponseEntity<?> getScreenById(BigInteger id);
}
