package com.microservice.erp.controllers.rest;

import com.microservice.erp.services.iServices.ISaUserSaRoleMapService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/userRoleMapping")
@AllArgsConstructor
public class SaUserSaRoleMapController {

    private final ISaUserSaRoleMapService service;

    @PostMapping
    public ResponseEntity<?> addSaUserSaRole(@RequestBody ISaUserSaRoleMapService.AddSaUserSaRoleCommand command) {
        return service.addSaUserSaRole(command);
    }
}
