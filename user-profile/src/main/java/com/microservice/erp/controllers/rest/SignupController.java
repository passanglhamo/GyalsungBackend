package com.microservice.erp.controllers.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.microservice.erp.domain.dto.NotificationRequestDto;
import com.microservice.erp.domain.dto.SignupRequestDto;
import com.microservice.erp.domain.repositories.IUserInfoRepository;
import com.microservice.erp.services.iServices.ISignupService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.wso2.client.api.ApiException;

import javax.validation.Valid;
import java.io.IOException;
import java.text.ParseException;
import java.util.Date;

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
    public ResponseEntity<?> receiveOtp(@RequestBody NotificationRequestDto notificationRequestDto) throws JsonProcessingException {
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
    public ResponseEntity<?> signup(@Valid @RequestBody SignupRequestDto signupRequestDto) throws ParseException, JsonProcessingException {
        return iSignupService.signup(signupRequestDto);
    }

    @GetMapping("/getExpectedPopulationByYear")
    public ResponseEntity<?> getExpectedUserDetails(@RequestParam("dateString") String dateString)
            throws IOException, ParseException {
        return iSignupService.getExpectedPopulationByYear(dateString);
    }

    @GetMapping("/getPersonDetailsByCid")
    public ResponseEntity<?> getPersonDetailsByCid(String cid) throws ParseException, IOException, ApiException {
        return iSignupService.getPersonDetailsByCid(cid);
    }

    @GetMapping("/getSignUpUsers")
    public ResponseEntity<?> getSignUpUsers(@RequestParam("dateString") String dateString) throws ParseException {
        return iSignupService.getSignUpUsers(dateString);
    }

}
