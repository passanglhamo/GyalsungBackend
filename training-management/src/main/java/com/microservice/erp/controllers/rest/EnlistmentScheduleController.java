package com.microservice.erp.controllers.rest;

import com.microservice.erp.domain.dto.EnlistmentScheduleDto;
import com.microservice.erp.domain.entities.EnlistmentSchedule;
import com.microservice.erp.services.iServices.ICreateEnlistmentScheduleService;
import com.microservice.erp.services.iServices.IReadEnlistmentScheduleService;
import com.microservice.erp.services.iServices.IUpdateEnlistmentScheduleService;
import com.microservice.erp.services.impl.services.SpringSecurityAuditorAware;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.math.BigInteger;
import java.text.ParseException;
import java.util.List;

@RestController
@RequestMapping("/enlistmentSchedules")
@AllArgsConstructor
public class EnlistmentScheduleController {

    private final ICreateEnlistmentScheduleService service;
    private final IReadEnlistmentScheduleService readService;
    private final IUpdateEnlistmentScheduleService updateService;

    @PostMapping
    public ResponseEntity<?> saveEnlistment(@RequestHeader(HttpHeaders.AUTHORIZATION) String token, @RequestBody EnlistmentScheduleDto enlistmentScheduleDto)
            throws IOException, ParseException {
        SpringSecurityAuditorAware.setToken(token);
        return service.saveEnlistmentSchedule(enlistmentScheduleDto);
    }

    @GetMapping
    public List<EnlistmentSchedule> getAllEnlistmentScheduleList() {

        return readService.getAllEnlistmentScheduleList();
    }

    @GetMapping("/getEnlistmentScheduleById")
    public EnlistmentSchedule getEnlistmentScheduleById(@RequestParam("id") BigInteger id) {
        return readService.getEnlistmentScheduleById(id);
    }

    @PutMapping("/updateEnlistmentSchedule")
    public ResponseEntity<?> updateEnlistment(@RequestHeader(HttpHeaders.AUTHORIZATION) String token,@RequestBody
    EnlistmentScheduleDto enlistmentScheduleDto) {
        SpringSecurityAuditorAware.setToken(token);
        return updateService.updateEnlistmentSchedule(enlistmentScheduleDto);
    }

}
