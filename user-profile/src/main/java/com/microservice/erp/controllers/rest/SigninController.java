package com.microservice.erp.controllers.rest;

import com.microservice.erp.domain.dto.JwtResponse;
import com.microservice.erp.domain.dto.LoginRequestDto;
import com.microservice.erp.domain.repositories.IUserInfoRepository;
import com.microservice.erp.domain.repositories.RoleRepository;
import com.microservice.erp.domain.security.jwt.JwtUtils;
import com.microservice.erp.domain.security.services.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/signin")
public class SigninController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    IUserInfoRepository iUserInfoRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequestDto loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getUserId(),
                userDetails.getFullName(),
                userDetails.getCid(),
                userDetails.getGender(),
                userDetails.getMobileNo(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                roles));
    }
}
