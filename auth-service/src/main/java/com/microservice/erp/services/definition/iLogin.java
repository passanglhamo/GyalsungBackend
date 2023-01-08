package com.microservice.erp.services.definition;

import com.microservice.erp.domain.models.LoginRequest;
import com.infoworks.lab.rest.models.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.IOException;

public interface iLogin {
    Response doLogin(LoginRequest request);

    Response isValidToken(String token, UserDetails principal);

    Response refreshToken(String token, UserDetails principal);

    Response doLogout(String token, UserDetails principal);
}

