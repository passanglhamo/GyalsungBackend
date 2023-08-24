package com.microservice.erp.controllers.rest;

import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;
import com.microservice.erp.domain.dto.DefermentDto;
import com.microservice.erp.domain.dto.DefermentListDto;
import com.microservice.erp.services.iServices.ICreateDefermentService;
import com.microservice.erp.services.iServices.IReadDefermentService;
import com.microservice.erp.services.iServices.IUpdateDefermentService;
import com.microservice.erp.services.impl.SpringSecurityAuditorAware;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.math.BigInteger;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("/deferment")
@AllArgsConstructor
public class DefermentController {

    private final ICreateDefermentService service;
    private final IReadDefermentService readService;
    private final IUpdateDefermentService updateService;


    @PostMapping
    public ResponseEntity<?> saveDeferment(HttpServletRequest request,
                                           @RequestHeader(HttpHeaders.AUTHORIZATION) String token,
                                           @ModelAttribute ICreateDefermentService.
                                                   CreateDefermentCommand command) throws Exception {
        SpringSecurityAuditorAware.setToken(token);
        return service.saveDeferment(request, command);
    }

    @GetMapping
    public List<DefermentDto> getAllDefermentList(@RequestHeader("Authorization") String authHeader) {
        return readService.getAllDefermentList(authHeader);
    }


    @GetMapping(value = "/getDefermentListByDefermentYearReasonStatus")
    public List<DefermentListDto> getDefermentListByDefermentYearReasonStatus(@RequestHeader("Authorization") String authHeader,
                                                                              @RequestParam("defermentYear") String defermentYear
            , @RequestParam("reasonId") BigInteger reasonId
            , @RequestParam("status") Character status
            , @RequestParam("gender") Character gender
            , @RequestParam("cid") String cid
            , @RequestParam("caseNumber") String caseNumber) {
        return readService.getDefermentListByDefermentYearReasonStatus(authHeader, defermentYear, reasonId, status, gender, cid, caseNumber);
    }

    @GetMapping(value = "/getDefermentByUserId")
    public ResponseEntity<?> getDefermentByUserId(@RequestParam("userId") BigInteger userId) {
        return readService.getDefermentByUserId(userId);
    }

    @GetMapping(value = "/getDefermentValidation")
    public ResponseEntity<?> getDefermentValidation(@RequestParam("userId") BigInteger userId) {
        return readService.getDefermentValidation(userId);
    }

    @GetMapping(value = "/getApprovedListByDefermentYearAndUserId")
    public List<DefermentDto> getApprovedListByDefermentYearAndUserId(@RequestHeader("Authorization") String authHeader,
                                                                      @RequestParam("defermentYear") String defermentYear
            , @RequestParam("userId") BigInteger userId) {
        return readService.getApprovedListByDefermentYearAndUserId(authHeader, defermentYear, userId);
    }

    @GetMapping(value = "/getDefermentListByUserId")
    public ResponseEntity<?> getDefermentListByUserId(@RequestParam("userId") BigInteger userId) {
        return readService.getDefermentListByUserId(userId);
    }

    @GetMapping(value = "/getDefermentAuditListByDefermentId")
    public List<DefermentDto> getDefermentAuditListByDefermentId(@RequestHeader("Authorization") String authHeader, @RequestParam("defermentId") BigInteger defermentId) {
        return readService.getDefermentAuditListByDefermentId(authHeader, defermentId);
    }

    @PostMapping(value = "/reviewRevertById")
    public ResponseEntity<?> reviewRevertById(@RequestHeader("Authorization") String authHeader,
                                              @RequestHeader(HttpHeaders.AUTHORIZATION) String token,
                                              @RequestBody IUpdateDefermentService.ReviewDefermentCommand command) throws Exception {
        SpringSecurityAuditorAware.setToken(token);
        return updateService.reviewRevertById(authHeader, command);
    }

    @PostMapping(value = "/approveRejectById")
    public ResponseEntity<?> approveRejectById(@RequestHeader("Authorization") String authHeader,
                                               @RequestHeader(HttpHeaders.AUTHORIZATION) String token,
                                               @RequestBody IUpdateDefermentService.ReviewDefermentCommand command) throws Exception {
        SpringSecurityAuditorAware.setToken(token);
        return updateService.approveRejectById(authHeader, command);
    }

    @PostMapping(value = "/mailSendToApplicant")
    public ResponseEntity<?> mailSendToApplicant(@RequestHeader("Authorization") String authHeader,
                                               @RequestHeader(HttpHeaders.AUTHORIZATION) String token,
                                               @RequestBody IUpdateDefermentService.ReviewDefermentCommand command) throws Exception {
        SpringSecurityAuditorAware.setToken(token);
        return updateService.mailSendToApplicant(authHeader, command);
    }

    @PostMapping(value = "/saveDraftById")
    public ResponseEntity<?> saveDraftById(@RequestHeader("Authorization") String authHeader,
                                               @RequestHeader(HttpHeaders.AUTHORIZATION) String token,
                                               @RequestBody IUpdateDefermentService.ReviewDefermentCommand command) throws Exception {
        SpringSecurityAuditorAware.setToken(token);
        return updateService.saveDraftById(authHeader, command);
    }
}
