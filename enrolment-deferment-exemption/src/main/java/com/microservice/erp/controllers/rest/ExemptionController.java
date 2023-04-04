package com.microservice.erp.controllers.rest;

import com.microservice.erp.domain.dto.ExemptionDto;
import com.microservice.erp.services.iServices.ICreateExemptionService;
import com.microservice.erp.services.iServices.IReadExemptionService;
import com.microservice.erp.services.iServices.IUpdateExemptionService;
import com.microservice.erp.services.impl.SpringSecurityAuditorAware;
import lombok.AllArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
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
        FileSystemResource file = new FileSystemResource(new File(url));
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

    @GetMapping(value = "/getExemptionListByCriteria")
    public List<ExemptionDto> getExemptionListByStatus(@RequestHeader("Authorization") String authHeader,
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
}
