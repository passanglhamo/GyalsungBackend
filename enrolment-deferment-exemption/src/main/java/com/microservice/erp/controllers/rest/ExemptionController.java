package com.microservice.erp.controllers.rest;

import com.microservice.erp.domain.dto.ExemptionDto;
import com.microservice.erp.domain.dto.ExemptionListDto;
import com.microservice.erp.services.iServices.ICreateExemptionService;
import com.microservice.erp.services.iServices.IReadExemptionService;
import com.microservice.erp.services.iServices.IUpdateDefermentService;
import com.microservice.erp.services.iServices.IUpdateExemptionService;
import com.microservice.erp.services.impl.SpringSecurityAuditorAware;
import lombok.AllArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.math.BigInteger;
import java.util.List;


@RestController
@RequestMapping("/exemption")
@AllArgsConstructor
public class ExemptionController {

    private final ICreateExemptionService service;
    private final IReadExemptionService readService;
    private final IUpdateExemptionService updateService;

    @PostMapping
    public ResponseEntity<?> saveExemption(HttpServletRequest request,
                                           @RequestHeader(HttpHeaders.AUTHORIZATION) String token,
                                           @ModelAttribute ICreateExemptionService.CreateExemptionCommand command) throws Exception {
        SpringSecurityAuditorAware.setToken(token);
        return service.saveExemption(request, command);
    }

    @GetMapping
    public List<ExemptionDto> getAllExemptionList(@RequestHeader("Authorization") String authHeader) {

        return readService.getAllExemptionList(authHeader);
    }

    @PostMapping(value = "/approveByIds")
    public ResponseEntity<?> approveByIds(@RequestHeader("Authorization") String authHeader,
                                          @RequestHeader(HttpHeaders.AUTHORIZATION) String token,
                                          @RequestBody IUpdateExemptionService.UpdateExemptionCommand command) {
        SpringSecurityAuditorAware.setToken(token);
        return updateService.approveByIds(authHeader, command);
    }

    @PostMapping(value = "/rejectByIds")
    public ResponseEntity<?> rejectByIds(@RequestHeader("Authorization") String authHeader,
                                         @RequestHeader(HttpHeaders.AUTHORIZATION) String token,
                                         @RequestBody IUpdateExemptionService.UpdateExemptionCommand command) {
        SpringSecurityAuditorAware.setToken(token);
        return updateService.rejectByIds(authHeader, command);
    }

    @RequestMapping(value = "/downloadFile", method = RequestMethod.GET)
    public ResponseEntity<?> downloadFile(@RequestParam("url") String url) {
        File file = new File(url);
        String contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE;

        if (file.exists()) {
            FileSystemResource resource = new FileSystemResource(file);
            contentType = determineContentType(file.getName());
            return ResponseEntity
                    .ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                    .contentType(MediaType.parseMediaType(contentType))
                    .body(resource);
        } else {
            // Handle file not found scenario
            return ResponseEntity.notFound().build();
        }
    }

    private String determineContentType(String fileName) {
        String extension = fileName.substring(fileName.lastIndexOf('.') + 1);
        switch (extension.toLowerCase()) {
            case "pdf":
                return "application/pdf";
            case "jpg":
            case "jpeg":
                return "image/jpeg";
            case "png":
                return "image/png";
            default:
                return MediaType.APPLICATION_OCTET_STREAM_VALUE;
        }
    }

    @GetMapping(value = "/getExemptionListByCriteria")
    public List<ExemptionListDto> getExemptionListByStatus(@RequestHeader("Authorization") String authHeader,
                                                           @RequestParam("exemptionYear") String exemptionYear
            , @RequestParam("reasonId") BigInteger reasonId
            , @RequestParam("status") Character status
            , @RequestParam("gender") Character gender
            , @RequestParam("cid") String cid) {
        return readService.getExemptionListByCriteria(authHeader,exemptionYear, status,reasonId,gender,cid);
    }

    @GetMapping(value = "/getExemptionByUserId")
    public ResponseEntity<?> getExemptionByUserId(@RequestParam("userId") BigInteger userId) {
        return readService.getExemptionByUserId(userId);
    }

    @GetMapping(value = "/getExemptionValidation")
    public ResponseEntity<?> getExemptionValidation(@RequestParam("userId") BigInteger userId) {
        return readService.getExemptionValidation(userId);
    }

    @GetMapping(value = "/getExemptionListByUserId")
    public ResponseEntity<?> getExemptionListByUserId(@RequestParam("userId") BigInteger userId) {
        return readService.getExemptionListByUserId(userId);
    }

    @PostMapping(value = "/saveToDraft")
    public ResponseEntity<?> saveToDraft(@RequestHeader("Authorization") String authHeader,
                                         @RequestHeader(HttpHeaders.AUTHORIZATION) String token,
                                         @RequestBody IUpdateExemptionService.UpdateExemptionCommand command) {
        SpringSecurityAuditorAware.setToken(token);
        return updateService.saveToDraft(authHeader, command);
    }
}
