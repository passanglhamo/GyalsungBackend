package com.microservice.erp.controllers.rest;

import com.microservice.erp.domain.dto.DefermentDto;
import com.microservice.erp.services.iServices.ICreateDefermentService;
import com.microservice.erp.services.iServices.IReadDefermentService;
import com.microservice.erp.services.iServices.IUpdateDefermentService;
import com.microservice.erp.services.impl.SpringSecurityAuditorAware;
import lombok.AllArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.math.BigInteger;
import java.util.Date;
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

    @PostMapping(value = "/approveByIds")
    public ResponseEntity<?> approveByIds(@RequestHeader("Authorization") String authHeader,
                                          @RequestHeader(HttpHeaders.AUTHORIZATION) String token,
                                          @RequestBody IUpdateDefermentService.UpdateDefermentCommand command) {
        SpringSecurityAuditorAware.setToken(token);
        return updateService.approveByIds(authHeader, command);
    }

    @PostMapping(value = "/rejectByIds")
    public ResponseEntity<?> rejectByIds(@RequestHeader("Authorization") String authHeader,
                                         @RequestHeader(HttpHeaders.AUTHORIZATION) String token,
                                         @RequestBody IUpdateDefermentService.UpdateDefermentCommand command) {
        SpringSecurityAuditorAware.setToken(token);
        return updateService.rejectByIds(authHeader, command);
    }

    @RequestMapping(value = "/downloadFile", method = RequestMethod.GET)
    public ResponseEntity<?> downloadFile(@RequestParam("url") String url) {
        FileSystemResource file = new FileSystemResource(new File(url));
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

    @GetMapping(value = "/getDefermentListByDefermentYearReasonStatus")
    public List<DefermentDto> getDefermentListByDefermentYearReasonStatus(@RequestHeader("Authorization") String authHeader,
                                                                          @RequestParam("defermentYear") String defermentYear
            , @RequestParam("reasonId") BigInteger reasonId
            , @RequestParam("status") Character status
            , @RequestParam("gender") Character gender
            , @RequestParam("cid") String cid) {
        return readService.getDefermentListByDefermentYearReasonStatus(authHeader, defermentYear, reasonId, status, gender, cid);
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

}
