package com.microservice.erp.services.iServices;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.microservice.erp.domain.dto.NotificationRequestDto;
import com.microservice.erp.domain.dto.SignupRequestDto;
import org.springframework.http.ResponseEntity;
import org.wso2.client.api.ApiException;

import java.io.IOException;
import java.text.ParseException;


public interface ISignupService {
    ResponseEntity<?> getCitizenDetails(String cid, String dob) throws ParseException, IOException, ApiException;

    ResponseEntity<?> receiveOtp(NotificationRequestDto notificationRequestDto) throws JsonProcessingException;

    ResponseEntity<?> verifyOtp(NotificationRequestDto notificationRequestDto);

    ResponseEntity<?> receiveEmailVcode(NotificationRequestDto notificationRequestDto) throws Exception;
    ResponseEntity<?> verifyEmailVcode(NotificationRequestDto notificationRequestDto);

    ResponseEntity<?> signup(SignupRequestDto signupRequestDto) throws ParseException;

}
