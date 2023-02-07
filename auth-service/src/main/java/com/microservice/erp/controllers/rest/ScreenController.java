package com.microservice.erp.controllers.rest;

import com.microservice.erp.domain.entities.Screen;
import com.microservice.erp.services.definition.IScreenService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigInteger;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/saScreens")
public class ScreenController {
    private final IScreenService iSaScreenService;

    public ScreenController(IScreenService iSaScreenService) {
        this.iSaScreenService = iSaScreenService;
    }

    @GetMapping("/getLastScreenId")
    public ResponseEntity<?> getLastScreenId() {
        return iSaScreenService.getLastScreenId();
    }

    @PostMapping
    public ResponseEntity<?> saveScreen(@Valid @RequestBody Screen saScreen) {
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

    @PutMapping
    public ResponseEntity<?> updateScreen(@Valid @RequestBody Screen saScreen) {
        return iSaScreenService.updateScreen(saScreen);
    }
}

