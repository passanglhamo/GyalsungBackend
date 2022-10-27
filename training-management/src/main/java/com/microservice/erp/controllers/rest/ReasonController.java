package com.microservice.erp.controllers.rest;

import com.microservice.erp.domain.entities.Reason;
import com.microservice.erp.services.iServices.ICreateReasonService;
import com.microservice.erp.services.iServices.IReadReasonService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/reasons")
@AllArgsConstructor
public class ReasonController {

    private final IReadReasonService readService;
    private final ICreateReasonService service;

    @PostMapping
    public Reason saveReason(@Valid @RequestBody Reason reason) {
        return service.saveReason(reason);
    }


    @GetMapping("/getAllReasonById")
    public Reason getAllReasonById(@RequestParam("id")Long id) {
        return readService.getAllReasonById(id);
    }

    @GetMapping
    public List<Reason> getAllReasonList() {
        return readService.getAllReasonList();
    }

    @GetMapping("/getAllReasonByStatus")
    public List<Reason> getAllReasonByStatus(@RequestParam("status") String status) {
        return readService.getAllReasonByStatus(status);
    }

}
