package com.microservice.erp.controllers.rest;

import com.microservice.erp.domain.dto.enrolment.EnrolmentDto;
import com.microservice.erp.services.iServices.IEnrolmentInfoService;
import com.microservice.erp.services.iServices.IUpdateDefermentService;
import lombok.AllArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.math.BigInteger;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/enrolment")
@AllArgsConstructor
public class EnrolmentController {

    private IEnrolmentInfoService iEnrolmentInfoService;

    @RequestMapping(value = "/getRegistrationDateInfo", method = RequestMethod.GET)
    public ResponseEntity<?> getRegistrationDateInfo() {
        return iEnrolmentInfoService.getRegistrationDateInfo();
    }

//    @RequestMapping(value = "/downloadParentConsentForm", method = RequestMethod.GET)
//    public ResponseEntity<?> downloadFile(@RequestParam("url") String url) {
//        FileSystemResource file = new FileSystemResource(new File(url));
//        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"").body(file);
//    }

    @PostMapping(value = "/saveEnrolment")
    public ResponseEntity<?> saveEnrolment(@RequestHeader("Authorization") String authHeader, @RequestBody EnrolmentDto enrolmentDto) throws Exception {
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
                                          @RequestBody IEnrolmentInfoService.EnrolmentInfoCommand command) {

        return iEnrolmentInfoService.allocateEnrolments(authHeader,command);
    }

}
