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

    @GetMapping(value = "/getRegisteredList")
    public ResponseEntity<?> getRegisteredList(@RequestHeader("Authorization") String authHeader, @RequestParam("year") String year) {
        return iDashBoardService.getRegisteredList(authHeader, year);
    }

    @GetMapping(value = "/getEarlyEnlistmentList")
    public ResponseEntity<?> getEarlyEnlistmentList(@RequestHeader("Authorization") String authHeader, @RequestParam("year") String year) {
        return iDashBoardService.getEarlyEnlistmentList(authHeader, year);
    }

    @GetMapping(value = "/getDeferredList")
    public ResponseEntity<?> getDeferredList(@RequestHeader("Authorization") String authHeader, @RequestParam("year") String year) {
        return iDashBoardService.getDeferredList(authHeader, year);
    }

    @GetMapping(value = "/getExemptedList")
    public ResponseEntity<?> getExemptedList(@RequestHeader("Authorization") String authHeader, @RequestParam("year") String year) {
        return iDashBoardService.getExemptedList(authHeader, year);
    }

    @GetMapping(value = "/getAcademyWiseEnrolmentFigure")
    public ResponseEntity<?> getAcademyWiseEnrolmentFigure(@RequestHeader("Authorization") String authHeader, @RequestParam("year") String year) {
        return iDashBoardService.getAcademyWiseEnrolmentFigure(authHeader, year);
    }

    @GetMapping(value = "/getTaskStatusByYear")
    public ResponseEntity<?> getTaskStatusByYear(@RequestHeader("Authorization") String authHeader, @RequestParam("year") String year) {
        return iDashBoardService.getTaskStatusByYear(authHeader, year);
    }
}
