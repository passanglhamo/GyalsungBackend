package com.microservice.erp.controllers.rest;

import com.microservice.erp.domain.entities.RegistrationDateInfo;
import com.microservice.erp.services.iServices.ICreateRegistrationDateInfoService;
import com.microservice.erp.services.iServices.IReadRegistrationDateInfoService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/registrationDateInfos")
@AllArgsConstructor
public class RegistrationDateInfoController {

    private final ICreateRegistrationDateInfoService service;
    private final IReadRegistrationDateInfoService readService;

    @PostMapping
    public RegistrationDateInfo saveRegistrationDateInfo(@Valid @RequestBody RegistrationDateInfo registrationDateInfo) {
        return service.saveRegistrationDateInfo(registrationDateInfo);
    }

    @GetMapping
    public List<RegistrationDateInfo> getAllRegistrationDateList() {
        return readService.getAllRegistrationDateList();
    }
}
