package com.microservice.erp.controllers.rest;


import com.microservice.erp.domain.dto.BookHospitalDto;
import com.microservice.erp.services.iServices.IBookHospitalService;
import com.microservice.erp.services.impl.SpringSecurityAuditorAware;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;

@RestController
@RequestMapping("/booking")
@AllArgsConstructor
public class BookHospitalController {
    private final IBookHospitalService iBookHospitalService;

    @PostMapping(value = "/getHospitalBookingByUserId")
    public ResponseEntity<?> getHospitalBookingByUserId(@RequestBody BookHospitalDto bookHospitalDto) {
        return iBookHospitalService.getHospitalBookingByUserId(bookHospitalDto);
    }

    @PostMapping(value = "/getPreviousBookingDetailByUserId")
    public ResponseEntity<?> getPreviousBookingDetailByUserId(@RequestHeader("Authorization") String authHeader
            , @RequestBody BookHospitalDto bookHospitalDto) {
        return iBookHospitalService.getPreviousBookingDetailByUserId(authHeader, bookHospitalDto);
    }

    @PostMapping(value = "/bookHospital")
    public ResponseEntity<?> bookHospital(@RequestHeader(HttpHeaders.AUTHORIZATION) String token,
                                          @RequestHeader("Authorization") String authHeader,
                                          @RequestBody BookHospitalDto bookHospitalDto) throws Exception {
        SpringSecurityAuditorAware.setToken(token);
        return iBookHospitalService.bookHospital(authHeader, bookHospitalDto);
    }


    @RequestMapping(value = "/getAllBookingByHospitalIdAndYear", method = RequestMethod.GET)
    public ResponseEntity<?> getAllBookingByHospitalIdAndYear(@RequestHeader("Authorization") String authHeader
            , @RequestParam("year") BigInteger year
            , @RequestParam("hospitalId") Integer hospitalId
    ) {
        return iBookHospitalService.getAllBookingByHospitalIdAndYear(authHeader, year, hospitalId);
    }

}
