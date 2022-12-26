package com.microservice.erp.services.iServices;

import com.microservice.erp.domain.dto.RoleDto;
import com.microservice.erp.domain.entities.SaRole;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ISaRoleService {
    ResponseEntity<?> saveRole(SaRole role);

    List<RoleDto> getAllRoleList();

     ResponseEntity<?> updateRole(RoleDto role);
}
