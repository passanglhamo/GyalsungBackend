package com.microservice.erp.controllers.rest;

import com.microservice.erp.domain.entities.ScreenGroup;
import com.microservice.erp.services.definition.IScreenGroupService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigInteger;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/saScreenGroups")
public class ScreenGroupController {
    private final IScreenGroupService iSaScreenGroupService;

    public ScreenGroupController(IScreenGroupService iSaScreenGroupService) {
        this.iSaScreenGroupService = iSaScreenGroupService;
    }

    @PostMapping
    public ResponseEntity<?> saveScreenGroup(@Valid @RequestBody ScreenGroup saScreenGroup) {
        return iSaScreenGroupService.saveScreenGroup(saScreenGroup);
    }

    @GetMapping()
    public ResponseEntity<?> getAllScreens() {
        return iSaScreenGroupService.getAllScreens();
    }

    @GetMapping("/getScreenById")
    public ResponseEntity<?> getScreenById(@RequestParam("id") BigInteger id) {
        return iSaScreenGroupService.getScreenById(id);
    }

}

