package com.microservice.erp.controllers.rest;

import com.microservice.erp.domain.entities.SaRole;
import com.microservice.erp.services.iServices.ISaRoleService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/role")
@AllArgsConstructor
public class SaRoleController {

    private final ISaRoleService service;
    @PostMapping
    public SaRole saveRole(@Valid @RequestBody SaRole role) {
        return service.saveRole(role);
    }
}
