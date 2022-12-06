package com.microservice.erp.controllers.rest;

import com.microservice.erp.domain.entities.DzongkhagHospitalMapping;
import com.microservice.erp.services.iServices.ICreateDzongkhagHospitalMappingService;
import com.microservice.erp.services.iServices.IReadDzongkhagHospitalMappingService;
import com.microservice.erp.services.iServices.IUpdateDzongkhagHospitalMappingService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/dzongkhagHospitalMappings")
@AllArgsConstructor
public class DzongkhagHospitalMappingController {

    private final ICreateDzongkhagHospitalMappingService service;
    private final IReadDzongkhagHospitalMappingService readService;
    private final IUpdateDzongkhagHospitalMappingService updateService;

    @PostMapping
    public ResponseEntity<?> saveDzongkhagHospital(@Valid @RequestBody DzongkhagHospitalMapping dzongkhagHospitalMapping) {
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

    @GetMapping(value = "/getAllActiveHospitalsByDzongkhagId", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAllActiveHospitalsByDzongkhagId(@RequestParam("dzongkhagId") Integer dzongkhagId) {
        return readService.getAllActiveHospitalsByDzongkhagId(dzongkhagId);
    }

    @PutMapping("/updateDzongkhagHospitalMapping")
    public ResponseEntity<?> updateDzongkhagHospitalMapping(@Valid @RequestBody DzongkhagHospitalMapping dzongkhagHospitalMapping) {

        return updateService.updateDzongkhagHospitalMapping(dzongkhagHospitalMapping);
    }

}
