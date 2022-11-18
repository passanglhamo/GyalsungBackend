package com.microservice.erp.controllers.rest;

import com.microservice.erp.domain.dto.enrolment.EnrolmentDto;
import com.microservice.erp.services.iServices.IEnrolmentInfoService;
import lombok.AllArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/enrolment")
@AllArgsConstructor
public class EnrolmentController {

    private IEnrolmentInfoService iEnrolmentInfoService;

    @RequestMapping(value = "/getActiveRegistrationInfo", method = RequestMethod.GET)
    public ResponseEntity<?> getActiveRegistrationInfo() {
        return iEnrolmentInfoService.getActiveRegistrationInfo();
    }

    @RequestMapping(value = "/downloadParentConsentForm", method = RequestMethod.GET)
    public ResponseEntity<?> downloadFile(@RequestParam("url") String url) {
        FileSystemResource file = new FileSystemResource(new File(url));
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

    @PostMapping(value = "/save", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> save(HttpServletRequest request, @ModelAttribute EnrolmentDto enrolmentDto) throws ParseException, IOException {
        return iEnrolmentInfoService.save(request, enrolmentDto);
    }

}
