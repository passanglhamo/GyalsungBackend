package com.microservice.erp.controllers.rest;

import com.microservice.erp.domain.entities.RegistrationDateInfo;
import com.microservice.erp.services.iServices.ICreateRegistrationDateInfoService;
import com.microservice.erp.services.iServices.IReadRegistrationDateInfoService;
import com.microservice.erp.services.iServices.IUpdateRegistrationDateInfoService;
import com.microservice.erp.services.impl.SpringSecurityAuditorAware;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/registrationDateInfos")
@AllArgsConstructor
public class RegistrationDateInfoController {

    private final ICreateRegistrationDateInfoService service;
    private final IReadRegistrationDateInfoService readService;
    private final IUpdateRegistrationDateInfoService updateService;

    @PostMapping
    public ResponseEntity<?> saveRegistrationDateInfo(@RequestHeader(HttpHeaders.AUTHORIZATION) String token,
                                                      @Valid @RequestBody RegistrationDateInfo registrationDateInfo) {
        SpringSecurityAuditorAware.setToken(token);
        return service.saveRegistrationDateInfo(registrationDateInfo);
    }

    @GetMapping
    public List<RegistrationDateInfo> getAllRegistrationDateList() {
        return readService.getAllRegistrationDateList();
    }

    @PutMapping("/updateRegistrationDateInfo")
    public ResponseEntity<?> updateRegistrationDateInfo(@RequestHeader(HttpHeaders.AUTHORIZATION) String token,
                                                        @Valid @RequestBody RegistrationDateInfo registrationDateInfo) {
        SpringSecurityAuditorAware.setToken(token);
        return updateService.updateRegistrationDateInfo(registrationDateInfo);
    }
}
