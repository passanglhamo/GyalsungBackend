package com.microservice.erp.controllers.rest.re;

import com.microservice.erp.domain.entities.Reason;
import com.microservice.erp.services.iServices.re.ICreateReasonService;
import com.microservice.erp.services.iServices.re.IReadReasonService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/reasons")
@AllArgsConstructor
public class ReasonController {

    private final IReadReasonService readService;
    private final ICreateReasonService service;

    @PostMapping
    public Reason insert(@Valid @RequestBody Reason reason) {
        return service.add(reason);
    }

    @GetMapping
    public List<Reason> query() {
        return readService.findAll();
    }

    @GetMapping("/{status}")
    public List<Reason> activeQuery(@PathVariable("status") Character status) {
        return readService.findAllByStatus(status);
    }

}
