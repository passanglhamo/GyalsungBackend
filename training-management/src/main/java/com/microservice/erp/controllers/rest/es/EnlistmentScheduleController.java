package com.microservice.erp.controllers.rest.es;

import com.microservice.erp.domain.dto.es.EnlistmentScheduleDto;
import com.microservice.erp.domain.entities.EnlistmentSchedule;
import com.microservice.erp.services.iServices.es.ICreateEnlistmentScheduleService;
import com.microservice.erp.services.iServices.es.IReadEnlistmentScheduleService;
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
    public ResponseEntity<?> save(@RequestBody EnlistmentScheduleDto enlistmentScheduleDto)
            throws IOException, ParseException {

        return service.save(enlistmentScheduleDto);
    }

    @GetMapping
    public List<EnlistmentSchedule> query() {

        return readService.findAll();
    }

    @GetMapping("/findById")
    public EnlistmentSchedule getById(@RequestParam("id") Long id) {
        return readService.findById(id);
    }

}
