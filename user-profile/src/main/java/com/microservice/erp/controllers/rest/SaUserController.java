package com.microservice.erp.controllers.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
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
    public ResponseEntity<?> addUser(@RequestBody UserDto userDto) throws JsonProcessingException {
        return iSaUserService.saveUser(userDto);
    }

    @PostMapping("/editUser")
    public ResponseEntity<?> editUser(@RequestBody UserDto userDto) throws JsonProcessingException {
        return iSaUserService.saveUser(userDto);
    }

    @GetMapping("/getUsers")
    public ResponseEntity<?> getUsers(@RequestHeader("Authorization") String authHeader) {
        return iSaUserService.getUsers(authHeader);
    }

    @GetMapping("/getAllRoles")
    public ResponseEntity<?> getAllRoles() {
        return iSaUserService.getAllRoles();
    }

}
