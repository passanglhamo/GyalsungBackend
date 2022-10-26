package com.microservice.erp.controllers.rest;

import com.microservice.erp.domain.entities.DzongkhagHospitalMapping;
import com.microservice.erp.services.iServices.ICreateDzongkhagHospitalMappingService;
import com.microservice.erp.services.iServices.IReadDzongkhagHospitalMappingService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/dzongkhagHospitalMappings")
@AllArgsConstructor
public class DzongkhagHospitalMappingController {

    private final ICreateDzongkhagHospitalMappingService service;
    private final IReadDzongkhagHospitalMappingService readService;

    @PostMapping
    public DzongkhagHospitalMapping insert(@Valid @RequestBody DzongkhagHospitalMapping dzongkhagHospitalMapping) {
        return service.add(dzongkhagHospitalMapping);
    }

    @GetMapping
    public List<DzongkhagHospitalMapping> query() {
        return readService.findAll();
    }

}
