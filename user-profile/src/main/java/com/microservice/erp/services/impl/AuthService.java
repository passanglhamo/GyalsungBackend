package com.microservice.erp.services.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.infoworks.lab.beans.tasks.definition.TaskStack;
import com.infoworks.lab.jjwt.TokenValidator;
import com.infoworks.lab.rest.models.Message;
import com.infoworks.lab.rest.models.Response;
import com.microservice.erp.controllers.rest.LoginRequest;
import com.microservice.erp.domain.repositories.ISaUserRepository;
import com.microservice.erp.services.iServices.IAuthService;
import com.microservice.erp.task.iam.CheckUserExist;
import com.microservice.erp.task.iam.Login;
import com.microservice.erp.task.iam.Logout;
import com.microservice.erp.task.iam.MakeTokenExpired;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;

@Service
@AllArgsConstructor
public class AuthService implements IAuthService {

    private final ISaUserRepository iSaUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleWiseAccessPermissionService roleWiseAccessPermissionService;


    @Override
    public ResponseEntity<?> doLogin(LoginRequest loginRequest) throws IOException {
//        LoginRetryCount count = cache.read(request.getUsername());
//        if (count != null) {
//            if (count.isMaxTryExceed()){
//                if (count.isTimePassed(blockDurationInMillis)) {
//                    count.resetFailedCount();
//                }else {
//                    long timeRemain = (blockDurationInMillis - count.timeElapsed()) / 1000;
//                    Response response = new Response()
//                            .setStatus(HttpStatus.FORBIDDEN.value())
//                            .setMessage("Please wait and try again " + timeRemain + " seconds later.");
//                    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response.toString());
//                }
//            }
//        }
        Response response = new Response().setMessage("Not Implemented").setStatus(HttpStatus.NOT_IMPLEMENTED.value());
        //
        loginRequest.setTokenTtl(3600000);
        TaskStack loginStack = TaskStack.createSync(true);
        loginStack.push(new CheckUserExist(iSaUserRepository, loginRequest.getUsername()));
        loginStack.push(new Login(iSaUserRepository, passwordEncoder, roleWiseAccessPermissionService, loginRequest));
        loginStack.commit(true, (message, state) -> {
            //LOG.info("Login Status: " + state.name());
            if (message != null)
                response.unmarshallingFromMap(message.marshallingToMap(true), true);
        });

        Map<String, Object> data = Message.unmarshal(new TypeReference<Map<String, Object>>() {
        }, response.getMessage());
        if (data == null) {
            return ResponseEntity.badRequest().body(response.getMessage());
        } else {
            return ResponseEntity.ok(data);
        }

    }

    @Override
    public ResponseEntity<?> isValidToken(String token) {
        token = TokenValidator.parseToken(token, "Bearer ");
        Response response = new CheckTokenValidity(token, iSaUserRepository).execute(null);
        if (response.getStatus() == HttpStatus.OK.value()) {
            return ResponseEntity.ok(response.toString());
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response.toString());
        }

    }

    @Override
    public ResponseEntity<?> doLogout(String token) {
        token = TokenValidator.parseToken(token, "Bearer ");
        Response response = new Response().setMessage("Not Implemented").setStatus(HttpStatus.NOT_IMPLEMENTED.value());
        //
        TaskStack logoutStack = TaskStack.createSync(true);
        logoutStack.push(new CheckTokenValidity(token, iSaUserRepository));
        logoutStack.push(new Logout(token));
        logoutStack.push(new MakeTokenExpired(token));
        logoutStack.commit(true, (message, state) -> {
            if (message != null)
                response.unmarshallingFromMap(message.marshallingToMap(true), true);
        });
        if (response.getStatus() == HttpStatus.OK.value()) {
            return ResponseEntity.ok(response.toString());
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response.toString());
        }
    }
}


