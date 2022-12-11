package com.microservice.erp.controllers.rest;

import com.microservice.erp.services.iServices.IDashBoardService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/dashboard")
@AllArgsConstructor
public class DashBoardController {
    private IDashBoardService iDashBoardService;

    @GetMapping(value = "/getRegistrationInfoFigures")
    public ResponseEntity<?> getRegistrationInfoFigures(@RequestParam("year") String year) {
        return iDashBoardService.getRegistrationInfoFigures(year);
    }
}
