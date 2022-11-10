package com.microservice.erp.controllers.rest;

import com.microservice.erp.domain.dto.MedicalBookingDto;
import com.microservice.erp.services.iServices.IMedicalBookingListService;
import com.microservice.erp.services.iServices.IMedicalBookingService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@AllArgsConstructor
@RequestMapping("/medicalBookingList")
public class MedicalBookingListController {
    private IMedicalBookingListService iMedicalBookingListService;

    @GetMapping(value = "/getAllBookingByHospitalIdAndYear")
    public ResponseEntity<?> getAllBookingByHospitalIdAndYear(@RequestParam("hospitalId") Long hospitalId
            , @RequestParam("year") String year) {
        return iMedicalBookingListService.getAllBookingByHospitalIdAndYear(hospitalId, year);
    }
}
