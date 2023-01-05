package com.microservice.erp.controllers.rest;

import com.microservice.erp.domain.dto.PermissionDto;
import com.microservice.erp.services.definition.IRoleWiseAccessPermissionService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;

@CrossOrigin(origins = "*")
@RestController
@AllArgsConstructor
@RequestMapping("/accessPermission")
public class RoleWiseAccessPermissionController {

    private final IRoleWiseAccessPermissionService iRoleWiseAccessPermissionService;

    @RequestMapping(value = "/getScreens", method = RequestMethod.GET)
    public ResponseEntity<?> getScreens(@RequestParam("roleId") BigInteger roleId) {
        return iRoleWiseAccessPermissionService.getScreens(roleId);
    }

    @RequestMapping(value = "/saveAccessPermission", method = RequestMethod.POST)
    public ResponseEntity<?> saveAccessPermission(@RequestBody PermissionDto permissionDto) {
        return iRoleWiseAccessPermissionService.saveAccessPermission(permissionDto);
    }

    //to save in bulk
    @RequestMapping(value = "/savePermission", method = RequestMethod.POST)
    public ResponseEntity<?> savePermission(@RequestBody PermissionDto permissionDto) {
        return iRoleWiseAccessPermissionService.savePermission(permissionDto);
    }
}

