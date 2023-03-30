package com.microservice.erp.controllers.rest;

import com.microservice.erp.services.iServices.IDashBoardService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/dashboard")
@AllArgsConstructor
public class DashBoardController {
    private final IDashBoardService iDashBoardService;

    @GetMapping(value = "/getEdeFigure")
    public ResponseEntity<?> getEdeFigure(@RequestParam("year") String year) {
        return iDashBoardService.getEdeFigure(year);
    }
}
