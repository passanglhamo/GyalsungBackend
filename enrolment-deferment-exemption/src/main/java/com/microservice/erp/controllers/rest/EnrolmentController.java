package com.microservice.erp.controllers.rest;

import com.microservice.erp.domain.dto.enrolment.EnrolmentDto;
import com.microservice.erp.domain.helper.MessageResponse;
import com.microservice.erp.services.iServices.IEnrolmentInfoService;
import com.microservice.erp.services.iServices.IUpdateDefermentService;
import lombok.AllArgsConstructor;
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

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/enrolment")
@AllArgsConstructor
public class EnrolmentController {
    private static Logger LOG = LoggerFactory.getLogger(EnrolmentController.class.getSimpleName());

    private IEnrolmentInfoService iEnrolmentInfoService;
    private KafkaTemplate<String, String> kafkaTemplate;
    private String enrolmentQueue;

    public void setKafkaTemplate(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void setEnrolmentQueue(String enrolmentQueue) {
        this.enrolmentQueue = enrolmentQueue;
    }

    @RequestMapping(value = "/getRegistrationDateInfo", method = RequestMethod.GET)
    public ResponseEntity<?> getRegistrationDateInfo() {
        return iEnrolmentInfoService.getRegistrationDateInfo();
    }

//    @RequestMapping(value = "/downloadParentConsentForm", method = RequestMethod.GET)
//    public ResponseEntity<?> downloadFile(@RequestParam("url") String url) {
//        FileSystemResource file = new FileSystemResource(new File(url));
//        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"").body(file);
//    }

    /*@PostMapping(value = "/saveEnrolment")
    public ResponseEntity<?> saveEnrolment(@RequestHeader("Authorization") String authHeader, @RequestBody EnrolmentDto enrolmentDto) throws Exception {
        return iEnrolmentInfoService.saveEnrolment(authHeader, enrolmentDto);
    }*/
	@PostMapping(value = "/saveEnrolment")
    public ResponseEntity<?> saveEnrolment(@RequestHeader("Authorization") String authHeader, @RequestBody EnrolmentDto enrolmentDto) throws Exception {

        ResponseEntity responseEntity = iEnrolmentInfoService.saveEnrolment(authHeader, enrolmentDto);

        if (responseEntity.getStatusCode().value() == 200) {
            kafkaTemplate.send(enrolmentQueue, "Please send mail");
        }

        return responseEntity;
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
                                                @RequestBody IEnrolmentInfoService.EnrolmentInfoCommand command) throws Exception {

        return iEnrolmentInfoService.allocateEnrolments(authHeader, command);
    }

}
