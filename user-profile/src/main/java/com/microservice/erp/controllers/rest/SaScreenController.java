/*
package com.microservice.erp.controllers.rest;

import com.microservice.erp.domain.entities.SaScreen;
import com.microservice.erp.services.iServices.ISaScreenService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigInteger;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/saScreens")
public class SaScreenController {
    private final ISaScreenService iSaScreenService;

    public SaScreenController(ISaScreenService iSaScreenService) {
        this.iSaScreenService = iSaScreenService;
    }

    @PostMapping
    public ResponseEntity<?> saveScreen(@Valid @RequestBody SaScreen saScreen) {
        return iSaScreenService.saveScreen(saScreen);
    }

    @GetMapping
    public ResponseEntity<?> getAllScreens() {
        return iSaScreenService.getAllScreens();
    }

    @GetMapping("/getScreenById")
    public ResponseEntity<?> getScreenById(@RequestParam("id") BigInteger id) {
        return iSaScreenService.getScreenById(id);
    }
}
*/
