package com.microservice.erp.controllers.rest;

import com.microservice.erp.domain.entities.SaScreenGroup;
import com.microservice.erp.services.iServices.ISaScreenGroupService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigInteger;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/screenGroups")
public class SaScreenGroupController {
    private final ISaScreenGroupService iSaScreenGroupService;

    public SaScreenGroupController(ISaScreenGroupService iSaScreenGroupService) {
        this.iSaScreenGroupService = iSaScreenGroupService;
    }

    @PostMapping
    public ResponseEntity<?> saveScreenGroup(@Valid @RequestBody SaScreenGroup saScreenGroup) {
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
