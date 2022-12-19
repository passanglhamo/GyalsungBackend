package com.microservice.erp.services.iServices;

import com.microservice.erp.controllers.rest.JwtResponse;
import com.microservice.erp.controllers.rest.LoginRequest;
import org.springframework.http.ResponseEntity;

import java.io.IOException;

public interface IAuthService {
    ResponseEntity<JwtResponse> doLogin(LoginRequest loginRequest) throws IOException;

    ResponseEntity<?> isValidToken(String token);
}
