package com.microservice.erp.controllers.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.microservice.erp.domain.dto.enrolment.EnrolmentDto;
import com.microservice.erp.domain.entities.EnrolmentInfo;
import com.microservice.erp.services.iServices.IEnrolmentInfoService;
import com.microservice.erp.services.impl.SpringSecurityAuditorAware;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.math.BigInteger;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/enrolment")
@AllArgsConstructor
public class EnrolmentController {

    private final IEnrolmentInfoService iEnrolmentInfoService;

    @RequestMapping(value = "/getRegistrationDateInfo", method = RequestMethod.GET)
    public ResponseEntity<?> getRegistrationDateInfo() {
        return iEnrolmentInfoService.getRegistrationDateInfo();
    }

    @RequestMapping(value = "/getMyEnrolmentInfo", method = RequestMethod.GET)
    public ResponseEntity<?> getMyEnrolmentInfo(@RequestParam("userId") BigInteger userId) {
        return iEnrolmentInfoService.getMyEnrolmentInfo(userId);
    }

    @PostMapping(value = "/saveEnrolment")
    public ResponseEntity<?> saveEnrolment(@RequestHeader("Authorization") String authHeader,
                                           @RequestHeader(HttpHeaders.AUTHORIZATION) String token,
                                           @RequestBody EnrolmentDto enrolmentDto) throws Exception {
        SpringSecurityAuditorAware.setToken(token);
        return iEnrolmentInfoService.saveEnrolment(authHeader, enrolmentDto);
    }

    @RequestMapping(value = "/getEnrolmentListByYearAndCoursePreference", method = RequestMethod.GET)
    public ResponseEntity<?> getEnrolmentListByYearAndCoursePreference(@RequestHeader("Authorization") String authHeader
            , @RequestParam("year") String year
            , @RequestParam("applicationStatus") Character applicationStatus
            , @RequestParam("courseId") BigInteger courseId
            , @RequestParam("coursePreferenceNumber") Integer coursePreferenceNumber
            , @RequestParam("cid") String cid) {
        return iEnrolmentInfoService.getEnrolmentListByYearAndCoursePreference(authHeader, year, applicationStatus, courseId, coursePreferenceNumber, cid);
    }


    @PostMapping(value = "/allocateEnrolments")
    public ResponseEntity<?> allocateEnrolments(@RequestHeader("Authorization") String authHeader,
                                                @RequestHeader(HttpHeaders.AUTHORIZATION) String token,
                                                @RequestBody IEnrolmentInfoService.EnrolmentInfoCommand command) throws Exception {
        SpringSecurityAuditorAware.setToken(token);
        return iEnrolmentInfoService.allocateEnrolments(authHeader, command);
    }

    @PostMapping(value = "/cancelEnrolments")
    public ResponseEntity<?> cancelEnrolments(@RequestHeader("Authorization") String authHeader,
                                              @RequestHeader(HttpHeaders.AUTHORIZATION) String token,
                                              @RequestBody IEnrolmentInfoService.EnrolmentInfoCommand command) throws Exception {
        SpringSecurityAuditorAware.setToken(token);
        return iEnrolmentInfoService.cancelEnrolments(authHeader, command);
    }

    @RequestMapping(value = "/getEnrolmentListByYearCourseAndAcademy", method = RequestMethod.GET)
    public ResponseEntity<?> getEnrolmentListByYearCourseAndAcademy(@RequestHeader("Authorization") String authHeader
            , @RequestParam("year") String year
            , @RequestParam("trainingAcademyId") Integer trainingAcademyId
            , @RequestParam("courseId") BigInteger courseId) {

        return iEnrolmentInfoService.getEnrolmentListByYearCourseAndAcademy(authHeader, year, trainingAcademyId, courseId);
    }


    @PostMapping(value = "/changeTrainingAcademy")
    public ResponseEntity<?> changeTrainingAcademy(@RequestHeader("Authorization") String authHeader,
                                                   @RequestHeader(HttpHeaders.AUTHORIZATION) String token,
                                                   @RequestBody IEnrolmentInfoService.EnrolmentInfoCommand command) throws Exception {
        SpringSecurityAuditorAware.setToken(token);
        return iEnrolmentInfoService.changeTrainingAcademy(authHeader, command);
    }

    @GetMapping(value = "/getEnrolmentValidation")
    public ResponseEntity<?> getEnrolmentValidation(@RequestParam("userId") BigInteger userId) {
        return iEnrolmentInfoService.getEnrolmentValidation(userId);
    }

    @RequestMapping(value = "/getEnrolmentListByYearAndAcademy", method = RequestMethod.GET)
    public List<EnrolmentInfo> getEnrolmentListByYearAndAcademy(@RequestHeader("Authorization") String authHeader
            , @RequestParam("year") String year
            , @RequestParam("trainingAcademyId") Integer trainingAcademyId) {

        return iEnrolmentInfoService.getEnrolmentListByYearAndAcademy(authHeader, year, trainingAcademyId);
    }

    @PostMapping(value = "/allocateUserToTrainingAca")
    public ResponseEntity<?> allocateUserToTrainingAca(@RequestHeader("Authorization") String authHeader,
                                                       @RequestHeader(HttpHeaders.AUTHORIZATION) String token,
                                                       @RequestParam("year") String year) throws IOException {
        SpringSecurityAuditorAware.setToken(token);
        return iEnrolmentInfoService.allocateUserToTrainingAca(authHeader,year);
    }

}
