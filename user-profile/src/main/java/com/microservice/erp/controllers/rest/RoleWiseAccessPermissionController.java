package com.microservice.erp.controllers.rest;

import com.microservice.erp.domain.dto.PermissionDto;
import com.microservice.erp.services.iServices.IRoleWiseAccessPermissionService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigInteger;

@CrossOrigin(origins = "*")
@RestController
@AllArgsConstructor
@RequestMapping("/accessPermission")
public class RoleWiseAccessPermissionController {

    private final IRoleWiseAccessPermissionService iRoleWiseAccessPermissionService;

    @RequestMapping(value = "/getScreens", method = RequestMethod.GET)
    public ResponseEntity<?> getScreens(BigInteger roleId) {
        return iRoleWiseAccessPermissionService.getScreens(roleId);
    }

    @RequestMapping(value = "/savePermission", method = RequestMethod.POST)
    public ResponseEntity<?> savePermission(PermissionDto permissionDto) {
        return iRoleWiseAccessPermissionService.savePermission(permissionDto);
    }
}
