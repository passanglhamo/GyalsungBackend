package com.microservice.erp.controllers.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.microservice.erp.domain.dto.UserDto;
import com.microservice.erp.services.iServices.ISaUserService;
import com.microservice.erp.services.impl.SpringSecurityAuditorAware;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.text.ParseException;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/saUser")
public class SaUserController {
    private final ISaUserService iSaUserService;

    public SaUserController(ISaUserService iSaUserService) {
        this.iSaUserService = iSaUserService;
    }

    @GetMapping("/getCensusDetailByCid")
    public ResponseEntity<?> getCensusDetailByCid(@RequestParam("cid") String cid) throws ParseException, IOException {
        return iSaUserService.getCensusDetailByCid(cid);
    }

    @PostMapping("/addUser")
    public ResponseEntity<?> addUser(@RequestBody UserDto userDto,
                                     @RequestHeader(HttpHeaders.AUTHORIZATION) String token) throws IOException, ParseException {
        SpringSecurityAuditorAware.setToken(token);
        return iSaUserService.saveUser(userDto);
    }

    @PostMapping("/editUser")
    public ResponseEntity<?> editUser(@RequestBody UserDto userDto,
                                      @RequestHeader(HttpHeaders.AUTHORIZATION) String token) throws IOException, ParseException {
        SpringSecurityAuditorAware.setToken(token);
        return iSaUserService.saveUser(userDto);
    }

    @GetMapping("/getUsers")
    public ResponseEntity<?> getUsers(@RequestHeader("Authorization") String authHeader) {
        return iSaUserService.getUsers(authHeader);
    }


    @GetMapping("/getOfficersByUserType")
    public ResponseEntity<?> getOfficersByUserType(@RequestHeader("Authorization") String authHeader,@RequestParam("userTypeVal") Character userTypeVal) {
        return iSaUserService.getOfficersByUserType(authHeader,userTypeVal);
    }
}
