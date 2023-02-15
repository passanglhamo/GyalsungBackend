package com.microservice.erp.controllers.rest;

import com.microservice.erp.domain.entities.PersonStatus;
import com.microservice.erp.services.iServices.IPersonStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/common")
@RequiredArgsConstructor
public class CommonController {

    private final IPersonStatusService personStatusService;

    @GetMapping("/getPersonStatus")
    public List<PersonStatus> getPersonStatus() {
        return personStatusService.getPersonStatus();
    }
}
