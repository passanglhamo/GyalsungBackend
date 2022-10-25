package com.microservice.erp.controllers.rest.hst;

import com.microservice.erp.domain.dto.hst.HospitalScheduleTimeDto;
import com.microservice.erp.domain.dto.hst.HospitalScheduleTimeListDto;
import com.microservice.erp.services.iServices.hst.ICreateHospitalScheduleTimeService;
import com.microservice.erp.services.iServices.hst.IReadHospitalScheduleTimeService;
import com.microservice.erp.services.iServices.hst.IUpdateHospitalScheduleTimeService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Collection;

@RestController
@RequestMapping("/hospitalScheduleTime")
@AllArgsConstructor
public class HospitalScheduleTimeController {

    private final ICreateHospitalScheduleTimeService service;
    private final IReadHospitalScheduleTimeService readService;
    private final IUpdateHospitalScheduleTimeService updateService;

    @PostMapping
    public ResponseEntity<?> save(@RequestBody HospitalScheduleTimeDto hospitalScheduleTimeDto) throws IOException {

        return service.save(hospitalScheduleTimeDto);
    }

    @GetMapping(value = "/getAllScheduleTimesById", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    public Collection<HospitalScheduleTimeDto> getAllScheduleTimesById(@RequestParam("dzoHosId") Long dzoHosId) {
        return readService.getAllScheduleTimesById(dzoHosId);
    }

    @PostMapping(value = "/updateScheduleTimes")
    public ResponseEntity<?> updateScheduleTimes(@RequestBody HospitalScheduleTimeListDto hospitalScheduleTimeListDto) throws IOException {

        return updateService.updateScheduleTimes(hospitalScheduleTimeListDto);
    }

    @GetMapping(value = "/getAllDzongkhag", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAllDzongkhag() {
        return readService.getAllDzongkhag();
    }

    @GetMapping(value = "/getAllActiveHospitalsByDzongkhagId", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAllActiveHospitalsByDzongkhagId(@RequestParam("dzongkhagId") Integer dzongkhagId) {
        return readService.getAllActiveHospitalsByDzongkhagId(dzongkhagId);
    }

    @GetMapping(value = "/getAllAvailableTimeSlotByHospitalId", produces = MediaType.APPLICATION_JSON_VALUE)
    public  ResponseEntity<?> getAllAvailableTimeSlotByHospitalId(@RequestParam("hospitalId") Long hospitalId) {
        return readService.getAllAvailableTimeSlotByHospitalId(hospitalId);
    }
}
