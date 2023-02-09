package com.microservice.erp.controllers.rest;

import com.microservice.erp.services.iServices.IMedicalBookingListService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;

@CrossOrigin(origins = "*")
@RestController
@AllArgsConstructor
@RequestMapping("/medicalBookingList")
public class MedicalBookingListController {
    private IMedicalBookingListService iMedicalBookingListService;

    @GetMapping(value = "/getAllBookingDateByHospitalIdAndYear")
    public ResponseEntity<?> getAllBookingDateByHospitalIdAndYear(@RequestParam("hospitalId") BigInteger hospitalId
            , @RequestParam("year") BigInteger year) {
        return iMedicalBookingListService.getAllBookingDateByHospitalIdAndYear(hospitalId, year);
    }

    @GetMapping(value = "/getTimeSlotsByScheduleDateId")
    public ResponseEntity<?> getTimeSlotsByScheduleDateId(@RequestHeader("Authorization") String authHeader,
                                                          @RequestParam("hospitalScheduleDateId")
                                                          BigInteger hospitalScheduleDateId) {
        return iMedicalBookingListService.getTimeSlotsByScheduleDateId(authHeader, hospitalScheduleDateId);
    }

    @GetMapping(value = "/getBookingDetail")
    public ResponseEntity<?> getBookingDetail(@RequestHeader("Authorization") String authHeader,
                                              @RequestParam("hospitalScheduleTimeId") BigInteger hospitalScheduleTimeId,
                                              @RequestParam("bookedById") BigInteger bookedById) {
        return iMedicalBookingListService.getBookingDetail(authHeader, hospitalScheduleTimeId, bookedById);
    }
}
