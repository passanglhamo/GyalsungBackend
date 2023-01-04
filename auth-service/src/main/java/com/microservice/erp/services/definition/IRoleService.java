package com.microservice.erp.services.definition;

import com.microservice.erp.domain.dto.RoleDto;
import com.microservice.erp.domain.entities.Role;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface IRoleService {
    ResponseEntity<?> saveRole(Role role);

    List<RoleDto> getAllRoleList();

    ResponseEntity<?> updateRole(RoleDto role);
}

