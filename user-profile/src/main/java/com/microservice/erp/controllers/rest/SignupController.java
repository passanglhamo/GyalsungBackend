package com.microservice.erp.controllers.rest;

import com.microservice.erp.domain.dto.NotificationRequestDto;
import com.microservice.erp.domain.dto.SignupRequestDto;
import com.microservice.erp.domain.entities.UserInfo;
import com.microservice.erp.services.iServices.ISignupService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.wso2.client.api.ApiException;

import javax.validation.Valid;
import java.io.IOException;
import java.math.BigInteger;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/signup")
public class SignupController {

    private final ISignupService iSignupService;

    public SignupController(ISignupService iSignupService) {
        this.iSignupService = iSignupService;
    }

    @GetMapping("/getCitizenDetails")
    public ResponseEntity<?> getCitizenDetails(String cid, String dob) throws ParseException, IOException, ApiException {
        return iSignupService.getCitizenDetails(cid, dob);
    }

    @PostMapping("/receiveOtp")
    public ResponseEntity<?> receiveOtp(@RequestBody NotificationRequestDto notificationRequestDto) {
        return iSignupService.receiveOtp(notificationRequestDto);
    }

    @PostMapping("/verifyOtp")
    public ResponseEntity<?> verifyOtp(@RequestBody NotificationRequestDto notificationRequestDto) {
        return iSignupService.verifyOtp(notificationRequestDto);
    }

    @PostMapping("/receiveEmailVcode")
    public ResponseEntity<?> receiveEmailVcode(@RequestBody NotificationRequestDto notificationRequestDto) throws Exception {
        return iSignupService.receiveEmailVcode(notificationRequestDto);
    }

    @PostMapping("/verifyEmailVcode")
    public ResponseEntity<?> verifyEmailVcode(@RequestBody NotificationRequestDto notificationRequestDto) {
        return iSignupService.verifyEmailVcode(notificationRequestDto);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody SignupRequestDto signupRequestDto) throws ParseException {
        return iSignupService.signup(signupRequestDto);
    }

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser() {

        List<String> roles = new ArrayList<>();
        roles.add("USER");

        return ResponseEntity.ok(new JwtResponse("staticToken",
                new BigInteger("2"),
                "Ngawang Zepa",
                "11514000454",
                'M',
                "17302667",
                "nzepa",
                "ngawang.zepa@thimphutechpark.bt",
                roles));
    }
}
