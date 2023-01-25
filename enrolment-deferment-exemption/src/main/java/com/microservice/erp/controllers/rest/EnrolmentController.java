package com.microservice.erp.controllers.rest;

import com.microservice.erp.domain.dto.enrolment.EnrolmentDto;
import com.microservice.erp.domain.helper.MessageResponse;
import com.microservice.erp.services.iServices.IEnrolmentInfoService;
import com.microservice.erp.services.iServices.IUpdateDefermentService;
import com.microservice.erp.services.impl.SpringSecurityAuditorAware;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.math.BigInteger;
import java.util.Objects;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/enrolment")
@AllArgsConstructor
public class EnrolmentController {
//    private static Logger LOG = LoggerFactory.getLogger(EnrolmentController.class.getSimpleName());

    private final IEnrolmentInfoService iEnrolmentInfoService;

    @RequestMapping(value = "/getRegistrationDateInfo", method = RequestMethod.GET)
    public ResponseEntity<?> getRegistrationDateInfo() {
        return iEnrolmentInfoService.getRegistrationDateInfo();
    }

    @RequestMapping(value = "/getMyEnrolmentInfo", method = RequestMethod.GET)
    public ResponseEntity<?> getMyEnrolmentInfo(@RequestParam("userId") BigInteger userId) {
        return iEnrolmentInfoService.getMyEnrolmentInfo(userId);
    }

//    @RequestMapping(value = "/downloadParentConsentForm", method = RequestMethod.GET)
//    public ResponseEntity<?> downloadFile(@RequestParam("url") String url) {
//        FileSystemResource file = new FileSystemResource(new File(url));
//        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"").body(file);
//    }

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
            , @RequestParam("courseId") BigInteger courseId
            , @RequestParam("coursePreferenceNumber") Integer coursePreferenceNumber) {
        return iEnrolmentInfoService.getEnrolmentListByYearAndCoursePreference(authHeader, year, courseId, coursePreferenceNumber);
    }

    @PostMapping(value = "/allocateEnrolments")
    public ResponseEntity<?> allocateEnrolments(@RequestHeader("Authorization") String authHeader,
                                                @RequestHeader(HttpHeaders.AUTHORIZATION) String token,
                                                @RequestBody IEnrolmentInfoService.EnrolmentInfoCommand command) throws Exception {
        SpringSecurityAuditorAware.setToken(token);
        return iEnrolmentInfoService.allocateEnrolments(authHeader, command);
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
}
