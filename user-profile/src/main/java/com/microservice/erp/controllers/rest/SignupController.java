package com.microservice.erp.controllers.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.microservice.erp.domain.dto.NotificationRequestDto;
import com.microservice.erp.domain.dto.SignupRequestDto;
import com.microservice.erp.services.iServices.IAgeCriteriaService;
import com.microservice.erp.services.iServices.ISignupService;
import lombok.SneakyThrows;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.wso2.client.api.ApiException;

import javax.validation.Valid;
import java.io.IOException;
import java.text.ParseException;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/signup")
public class SignupController {
    private final IAgeCriteriaService iAgeCriteriaService;
    private final ISignupService iSignupService;

    public SignupController(IAgeCriteriaService iAgeCriteriaService, ISignupService iSignupService) {
        this.iAgeCriteriaService = iAgeCriteriaService;
        this.iSignupService = iSignupService;
    }

    @GetMapping(value = "/getAgeCriteria")
    public ResponseEntity<?> getAgeCriteria() {
        return iAgeCriteriaService.getAgeCriteria();
    }

    @GetMapping("/getCitizenDetails")
    public ResponseEntity<?> getCitizenDetails(String cid, String dob) throws ParseException, IOException, ApiException {
        return iSignupService.getCitizenDetails(cid, dob);
    }

    @GetMapping("/validateCitizenDetails")
    public ResponseEntity<?> validateCitizenDetails(String cid, String dob) throws ParseException, IOException, ApiException {
        return iSignupService.validateCitizenDetails(cid, dob);
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

    @SneakyThrows
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody SignupRequestDto signupRequestDto) throws ParseException, IOException, ApiException {
        return iSignupService.signup(signupRequestDto);
    }

    @GetMapping("/getPersonDetailsByCid")
    public ResponseEntity<?> getPersonDetailsByCid(String cid) throws ParseException, IOException, ApiException {
        return iSignupService.getPersonDetailsByCid(cid);
    }

    @GetMapping("/getSignUpUsers")
    public ResponseEntity<?> getSignUpUsers(@RequestParam("dateString") String dateString) throws ParseException {
        return iSignupService.getSignUpUsers(dateString);
    }

    @GetMapping("/getEligiblePopulationByYearAndAge")
    public ResponseEntity<?> getEligiblePopulationByYearAndAge(@RequestParam("dateString") String dateString)
            throws IOException, ParseException, UnirestException {
        return iSignupService.getEligiblePopulationByYearAndAge(dateString);
    }

    @GetMapping("/getListOfStudentsByClassAndYear")
    public ResponseEntity<?> getListOfStudentsByClassAndYear(@RequestParam("className") String className, @RequestParam("year") String year)
            throws IOException, ParseException, UnirestException {
        return iSignupService.getListOfStudentsByClassAndYear(className, year);
    }

}
