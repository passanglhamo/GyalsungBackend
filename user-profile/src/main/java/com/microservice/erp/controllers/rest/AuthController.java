package com.microservice.erp.controllers.rest;

import com.microservice.erp.services.iServices.IAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final IAuthService iAuthService;

    public AuthController(@Autowired IAuthService iAuthService) {
        this.iAuthService = iAuthService;
    }


    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) throws IOException {

        return iAuthService.doLogin(loginRequest);
    }

    @GetMapping("/validateToken")
    public ResponseEntity<?> validateToken(@RequestParam("token") String token) {
        return iAuthService.isValidToken(token);

    }
}
