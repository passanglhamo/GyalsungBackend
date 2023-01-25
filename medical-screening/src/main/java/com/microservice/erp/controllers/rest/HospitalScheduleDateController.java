package com.microservice.erp.controllers.rest;

import com.microservice.erp.domain.dto.HospitalScheduleDateDto;
import com.microservice.erp.domain.dto.HospitalScheduleTimeDto;
import com.microservice.erp.services.iServices.ICreateHospitalScheduleDateService;
import com.microservice.erp.services.iServices.IReadHospitalScheduleDateService;
import com.microservice.erp.services.iServices.IUpdateHospitalScheduleTimeService;
import com.microservice.erp.services.impl.services.SpringSecurityAuditorAware;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Collection;

@RestController
@RequestMapping("/hospitalScheduleDate")
@AllArgsConstructor
public class HospitalScheduleDateController {

    private final ICreateHospitalScheduleDateService service;
    private final IReadHospitalScheduleDateService readService;
    private final IUpdateHospitalScheduleTimeService updateService;

    @PostMapping
    public ResponseEntity<?> saveScheduleDate(@RequestHeader(HttpHeaders.AUTHORIZATION) String token,
                                              @RequestBody HospitalScheduleDateDto hospitalScheduleTimeDto)
            throws IOException {
        SpringSecurityAuditorAware.setToken(token);
        return service.saveScheduleDate(hospitalScheduleTimeDto);
    }

    @GetMapping(value = "/getAllScheduleTimesById", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    public Collection<HospitalScheduleDateDto> getAllScheduleTimesById(@RequestParam("dzoHosId") BigInteger dzoHosId) {
        return readService.getAllScheduleDateById(dzoHosId);
    }

    @PostMapping(value = "/updateScheduleTimes")
    public ResponseEntity<?> updateScheduleTimes(@RequestHeader(HttpHeaders.AUTHORIZATION) String token,
                                                 @RequestBody HospitalScheduleTimeDto hospitalScheduleTimeListDto) throws IOException {
        SpringSecurityAuditorAware.setToken(token);
        return updateService.updateScheduleTimes(hospitalScheduleTimeListDto);
    }


    @GetMapping(value = "/getAllAvailableAppointmentDateByHospitalId", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAllAvailableAppointmentDateByHospitalId(@RequestParam("hospitalId") BigInteger hospitalId) {
        return readService.getAllAvailableAppointmentDateByHospitalId(hospitalId);
    }
}
