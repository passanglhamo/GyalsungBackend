/*
package com.microservice.erp.controllers.rest;

import com.microservice.erp.domain.dto.RoleDto;
import com.microservice.erp.domain.entities.SaRole;
import com.microservice.erp.services.iServices.ISaRoleService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/role")
@AllArgsConstructor
public class SaRoleController {

    private final ISaRoleService service;

    @PostMapping
    public ResponseEntity<?> saveRole(@Valid @RequestBody SaRole role) {
        return service.saveRole(role);
    }

    @GetMapping
    public List<RoleDto> getAllRoleList() {

        return service.getAllRoleList();
    }

    @PutMapping
    public ResponseEntity<?> updateRole(@Valid @RequestBody RoleDto roleDto) {

        return service.updateRole(roleDto);
    }
}
*/
