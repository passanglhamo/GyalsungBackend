/*
package com.microservice.erp.controllers.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.infoworks.lab.jjwt.JWTPayload;
import com.infoworks.lab.jjwt.TokenValidator;
import com.microservice.erp.domain.entities.SaUser;
import com.microservice.erp.domain.repositories.IUserInfoRepository;
import com.microservice.erp.services.iServices.IUserInfoService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

*/
/**
 * @author Rajib Kumer Ghosh
 *//*

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/userinfo")
public class UserInfoController {

    private IUserInfoService service;
    private ObjectMapper mapper;
    private IUserInfoRepository iUserInfoRepository;

    public UserInfoController(IUserInfoService service, ObjectMapper mapper, IUserInfoRepository iUserInfoRepository) {
        this.service = service;
        this.mapper = mapper;
        this.iUserInfoRepository = iUserInfoRepository;
    }
    @GetMapping("/hello")
    public ResponseEntity<String> getHello(@RequestHeader(HttpHeaders.AUTHORIZATION) String token) throws JsonProcessingException {
        JWTPayload jwtPayload = TokenValidator.parsePayload(token, JWTPayload.class);
        String userName = jwtPayload.getSub();
        Integer count = 121;
        return ResponseEntity.ok(mapper.writeValueAsString("Transaction Counts:- " + count + ", User Name:- " + userName));
    }

    @GetMapping("/rowCount")
    public Long getRowCount() {
        Long count = service.totalCount();
        return count;
    }

    @GetMapping
    public List<SaUser> query(@RequestParam("limit") Integer size
            , @RequestParam("page") Integer page) {
        //TODO: Test with RestExecutor
        List<SaUser> saUsers = service.findAll(page, size);
        return saUsers;
    }

    @PostMapping
    public SaUser insert(@Valid @RequestBody SaUser saUser) {
        //TODO: Test with RestExecutor
        SaUser nSaUser = service.add(saUser);
        return nSaUser;
    }

    @PutMapping
    public SaUser update(@Valid @RequestBody SaUser saUser) {
        //TODO: Test with RestExecutor
        SaUser old = service.update(saUser);
        return old;
    }

    @DeleteMapping
    public Boolean delete(@RequestParam("userid") Long userId) {
        //TODO: Test with RestExecutor
        boolean deleted = service.remove(userId);
        return deleted;
    }
}
*/
