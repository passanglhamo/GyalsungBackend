package com.microservice.erp.services.impl;

import com.infoworks.lab.beans.tasks.definition.TaskStack;
import com.infoworks.lab.rest.models.Response;
import com.microservice.erp.controllers.rest.JwtResponse;
import com.microservice.erp.domain.models.LoginRequest;
import com.microservice.erp.domain.repositories.UserRepository;
import com.microservice.erp.domain.tasks.iam.CheckTokenValidity;
import com.microservice.erp.domain.tasks.iam.Logout;
import com.microservice.erp.domain.tasks.iam.MakeTokenExpired;
import com.microservice.erp.domain.tasks.iam.RefreshToken;
import com.microservice.erp.services.definition.iLogin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.time.Duration;

@Service
public class LoginService implements iLogin {

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
    public ResponseEntity<?> doLogin(LoginRequest request) throws IOException {

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(request.toString(), headers);

        String userUrl = "http://localhost:8084/api/user/profile/auth/signin";
        return restTemplate.exchange(userUrl, HttpMethod.POST, entity, String.class);

    }

    @Override
    public ResponseEntity<?> isValidToken(String token, UserDetails principal) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(principal.toString(), headers);

        String userUrl = "http://localhost:8084/api/user/profile/auth/validateToken?token=" + token;

        return restTemplate.exchange(userUrl, HttpMethod.GET, entity, String.class);
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
