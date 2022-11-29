package com.microservice.erp.controllers.rest;

import com.microservice.erp.services.iServices.IAcademyWiseInfoService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;

@RestController
@RequestMapping("/academyWiseInfo")
@AllArgsConstructor
public class AcademyWiseInfoController {
    private IAcademyWiseInfoService iAcademyWiseInfoService;

    @RequestMapping(value = "/getEnrolmentFigureByYear", method = RequestMethod.GET)
    public ResponseEntity<?> getEnrolmentFigureByYear(@RequestHeader("Authorization") String authHeader, @RequestParam("year") String year) {
        return iAcademyWiseInfoService.getEnrolmentFigureByYear(authHeader, year);
    }

}
