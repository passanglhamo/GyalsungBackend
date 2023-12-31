package com.microservice.erp.services.iServices;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.microservice.erp.domain.dto.NotificationRequestDto;
import com.microservice.erp.domain.dto.SignupRequestDto;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;


public interface ISignupService {
    ResponseEntity<?> getCitizenDetails(String cid, String dob) throws ParseException, IOException;

    ResponseEntity<?> validateCitizenDetails(String cid, String dob) throws ParseException, IOException;

    ResponseEntity<?> receiveOtp(NotificationRequestDto notificationRequestDto) throws JsonProcessingException;

    ResponseEntity<?> verifyOtp(NotificationRequestDto notificationRequestDto);

    ResponseEntity<?> receiveEmailVcode(NotificationRequestDto notificationRequestDto) throws Exception;

    ResponseEntity<?> verifyEmailVcode(NotificationRequestDto notificationRequestDto);

    ResponseEntity<?> signup(SignupRequestDto signupRequestDto) throws ParseException, IOException;

    ResponseEntity<?> getPersonDetailsByCid(String cid) throws IOException, ParseException;

    ResponseEntity<?> getSignUpUsers(String tillDate) throws ParseException;

    ResponseEntity<?> getEligiblePopulationByYearAndAge(String dateString) throws IOException, ParseException, UnirestException;

    ResponseEntity<?> getListOfStudentsByClassAndYear(String className, String year) throws IOException, ParseException, UnirestException;
}
