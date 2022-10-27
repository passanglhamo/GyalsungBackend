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
    public DzongkhagHospitalMapping saveDzongkhagHospital(@Valid @RequestBody DzongkhagHospitalMapping dzongkhagHospitalMapping) {
        return service.saveDzongkhagHospital(dzongkhagHospitalMapping);
    }

    @GetMapping
    public List<DzongkhagHospitalMapping> getAllDzongkhagHospitalList() {
        return readService.getAllDzongkhagHospitalList();
    }

    @GetMapping("/getAllDzongkhagHosByStatus")
    public List<DzongkhagHospitalMapping> getAllDzongkhagHosByStatus(@RequestParam("status") String status) {
        return readService.getAllDzongkhagHosByStatus(status);
    }

}
