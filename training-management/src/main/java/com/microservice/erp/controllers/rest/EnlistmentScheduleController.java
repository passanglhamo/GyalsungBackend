package com.microservice.erp.controllers.rest;

import com.microservice.erp.domain.dto.EnlistmentScheduleDto;
import com.microservice.erp.domain.entities.EnlistmentSchedule;
import com.microservice.erp.services.iServices.ICreateEnlistmentScheduleService;
import com.microservice.erp.services.iServices.IReadEnlistmentScheduleService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

@RestController
@RequestMapping("/enlistmentSchedules")
@AllArgsConstructor
public class EnlistmentScheduleController {

    private final ICreateEnlistmentScheduleService service;
    private final IReadEnlistmentScheduleService readService;

    @PostMapping
    public ResponseEntity<?> saveEnlistment(@RequestBody EnlistmentScheduleDto enlistmentScheduleDto)
            throws IOException, ParseException {

        return service.saveEnlistmentSchedule(enlistmentScheduleDto);
    }

    @GetMapping
    public List<EnlistmentSchedule> getAllEnlistmentScheduleList() {

        return readService.getAllEnlistmentScheduleList();
    }

    @GetMapping("/getEnlistmentScheduleById")
    public EnlistmentSchedule getEnlistmentScheduleById(@RequestParam("id") Long id) {
        return readService.getEnlistmentScheduleById(id);
    }

}
