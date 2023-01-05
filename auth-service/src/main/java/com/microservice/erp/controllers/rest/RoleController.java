
package com.microservice.erp.controllers.rest;

import com.microservice.erp.domain.dto.RoleDto;
import com.microservice.erp.domain.entities.Role;
import com.microservice.erp.services.definition.IRoleService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/role")
@AllArgsConstructor
public class RoleController {

    private final IRoleService service;

    @PostMapping
    public ResponseEntity<?> saveRole(@Valid @RequestBody Role role) {
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

