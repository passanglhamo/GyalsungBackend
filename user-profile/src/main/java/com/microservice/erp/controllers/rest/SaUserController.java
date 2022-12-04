package com.microservice.erp.controllers.rest;

import com.microservice.erp.domain.dto.UserDto;
import com.microservice.erp.services.iServices.ISaUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/saUser")
public class SaUserController {
    private final ISaUserService iSaUserService;

    public SaUserController(ISaUserService iSaUserService) {
        this.iSaUserService = iSaUserService;
    }

    @PostMapping("/addUser")
    public ResponseEntity<?> addUser(@RequestBody UserDto userDto) {
        return iSaUserService.addUser(userDto);
    }

    @GetMapping("/getAllRoles")
    public ResponseEntity<?> getAllRoles() {
        return iSaUserService.getAllRoles();
    }

}
