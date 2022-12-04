package com.microservice.erp.controllers.rest;

import com.microservice.erp.domain.dto.MessageResponse;
import com.microservice.erp.domain.dto.NotificationRequestDto;
import com.microservice.erp.domain.dto.SignupRequestDto;
import com.microservice.erp.domain.entities.SaUser;
import com.microservice.erp.domain.repositories.ISaUserRepository;
import com.microservice.erp.services.iServices.ISignupService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.wso2.client.api.ApiException;

import javax.validation.Valid;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/signup")
public class SignupController {

    private final ISignupService iSignupService;
    private final ISaUserRepository iSaUserRepository;

    public SignupController(ISignupService iSignupService, ISaUserRepository iSaUserRepository) {
        this.iSignupService = iSignupService;
        this.iSaUserRepository = iSaUserRepository;
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

        SaUser saUser = iSaUserRepository.findByCid(loginRequest.getUsername());
        if (saUser == null) {
            ResponseEntity.badRequest().body(new MessageResponse("Invalid username or password."));
        }

        assert saUser != null;

        return ResponseEntity.ok(new JwtResponse("staticToken",
                saUser.getId(),
                saUser.getFullName(),
                saUser.getCid(),
                saUser.getGender(),
                saUser.getMobileNo(),
                saUser.getUsername(),
                saUser.getEmail(),
                roles));
    }
}
