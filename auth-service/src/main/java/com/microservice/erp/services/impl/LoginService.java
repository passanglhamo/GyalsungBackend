package com.microservice.erp.services.impl;

import com.infoworks.lab.beans.tasks.definition.TaskStack;
import com.microservice.erp.domain.models.LoginRequest;
import com.microservice.erp.domain.repositories.UserRepository;
import com.microservice.erp.domain.tasks.iam.*;
import com.infoworks.lab.rest.models.Response;
import com.microservice.erp.services.definition.iLogin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class LoginService implements iLogin{

    private static Logger LOG = LoggerFactory.getLogger(LoginService.class.getSimpleName());
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    public LoginService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Value("${app.login.token.ttl.duration.millis}")
    private long tokenTtl;

    @Override
    public Response doLogin(LoginRequest request) {
        Response response = new Response().setMessage("Not Implemented").setStatus(HttpStatus.NOT_IMPLEMENTED.value());
        //
        request.setTokenTtl(tokenTtl);
        TaskStack loginStack = TaskStack.createSync(true);
        loginStack.push(new CheckUserExist(userRepository, request.getUsername()));
        loginStack.push(new Login(userRepository, passwordEncoder, request));
        loginStack.commit(true, (message, state) -> {
            LOG.info("Login Status: " + state.name());
            if (message != null)
                response.unmarshallingFromMap(message.marshallingToMap(true), true);
        });
        //
        return response;
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
