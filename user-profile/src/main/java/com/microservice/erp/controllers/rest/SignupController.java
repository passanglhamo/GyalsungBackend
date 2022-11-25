package com.microservice.erp.controllers.rest;

import com.microservice.erp.domain.dto.MessageResponse;
import com.microservice.erp.domain.dto.NotificationRequestDto;
import com.microservice.erp.domain.dto.SignupRequestDto;
import com.microservice.erp.domain.entities.UserInfo;
import com.microservice.erp.domain.repositories.IUserInfoRepository;
import com.microservice.erp.services.iServices.ISignupService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.wso2.client.api.ApiException;

import javax.validation.Valid;
import java.io.IOException;
import java.math.BigInteger;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/signup")
public class SignupController {

    private final ISignupService iSignupService;
    private final IUserInfoRepository iUserInfoRepository;

    public SignupController(ISignupService iSignupService, IUserInfoRepository iUserInfoRepository) {
        this.iSignupService = iSignupService;
        this.iUserInfoRepository = iUserInfoRepository;
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
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {

        List<String> roles = new ArrayList<>();
        roles.add("USER");

        UserInfo userInfo = iUserInfoRepository.findByCid(loginRequest.getUsername());
        if (userInfo == null) {
            ResponseEntity.badRequest().body(new MessageResponse("Invalid username or password."));
        }

        assert userInfo != null;

        return ResponseEntity.ok(new JwtResponse("staticToken",
                userInfo.getId(),
                userInfo.getFullName(),
                userInfo.getCid(),
                userInfo.getSex(),
                userInfo.getMobileNo(),
                userInfo.getUsername(),
                userInfo.getEmail(),
                roles));
    }
}
