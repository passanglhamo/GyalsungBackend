package com.microservice.erp.controllers.rest;

import com.microservice.erp.services.iServices.IRoleWiseAccessPermissionService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*")
@RestController
@AllArgsConstructor
@RequestMapping("/accessPermission")
public class RoleWiseAccessPermissionController {

    private IRoleWiseAccessPermissionService iRoleWiseAccessPermissionService;


}
