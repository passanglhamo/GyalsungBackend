package com.microservice.erp.services.impl;

import com.infoworks.lab.beans.tasks.definition.TaskStack;
import com.infoworks.lab.rest.models.Message;
import com.infoworks.lab.rest.models.Response;
import com.microservice.erp.domain.dto.MessageResponse;
import com.microservice.erp.domain.models.LoginRequest;
import com.microservice.erp.domain.repositories.UserRepository;
import com.microservice.erp.domain.tasks.iam.*;
import com.microservice.erp.services.definition.iLogin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.core.type.TypeReference;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;

@Service
public class LoginService implements iLogin {

    private static Logger LOG = LoggerFactory.getLogger(LoginService.class.getSimpleName());
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private RoleWiseAccessPermissionService roleWiseAccessPermissionService;


    public LoginService(UserRepository userRepository, PasswordEncoder passwordEncoder, RoleWiseAccessPermissionService roleWiseAccessPermissionService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleWiseAccessPermissionService = roleWiseAccessPermissionService;
    }

    @Value("${app.login.token.ttl.duration.millis}")
    private long tokenTtl;

    @Override
    public ResponseEntity<?> doLogin(LoginRequest request) throws IOException {
        Response response = new Response().setMessage("Not Implemented").setStatus(HttpStatus.NOT_IMPLEMENTED.value());
        //
        request.setTokenTtl(tokenTtl);
        TaskStack loginStack = TaskStack.createSync(true);
        loginStack.push(new CheckUserExist(userRepository, request.getUsername()));
        loginStack.push(new Login(userRepository, passwordEncoder, roleWiseAccessPermissionService, request));
        loginStack.commit(true, (message, state) -> {
            LOG.info("Login Status: " + state.name());
            if (message != null)
                response.unmarshallingFromMap(message.marshallingToMap(true), true);
        });

        Map<String, Object> data = Message.unmarshal(new TypeReference<Map<String, Object>>() {
        }, response.getMessage());
        if (data == null) {
            return ResponseEntity.badRequest().body(new MessageResponse(response.getMessage()));
        } else {
            return ResponseEntity.ok(data);
        }
    }

    @Override
    public Response isValidToken(String token, UserDetails principal) {
        return new CheckTokenValidity(token, userRepository).execute(null);
    }

    @Override
    public Response refreshToken(String token, UserDetails principal) {
        return new RefreshToken(token, userRepository, Duration.ofMillis(tokenTtl)).execute(null);
    }

    @Override
    public Response doLogout(String token, UserDetails principal) {
        Response response = new Response().setMessage("Not Implemented").setStatus(HttpStatus.NOT_IMPLEMENTED.value());
        //
        TaskStack logoutStack = TaskStack.createSync(true);
        logoutStack.push(new CheckTokenValidity(token, userRepository));
        logoutStack.push(new Logout(token));
        logoutStack.push(new MakeTokenExpired(token));
        logoutStack.commit(true, (message, state) -> {
            LOG.info("Logout Status: " + state.name());
            if (message != null)
                response.unmarshallingFromMap(message.marshallingToMap(true), true);
        });
        //
        return response;
    }
}
