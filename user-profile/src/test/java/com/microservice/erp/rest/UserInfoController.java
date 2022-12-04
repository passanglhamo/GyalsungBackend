package com.microservice.erp.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservice.erp.domain.entities.SaUser;
import com.microservice.erp.services.iServices.IUserInfoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @author Rajib Kumer Ghosh
 *
 */

@RestController
@RequestMapping("/userinfo")
public class UserInfoController {

    private IUserInfoService service;
    private ObjectMapper mapper;

    public UserInfoController(IUserInfoService service, ObjectMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @GetMapping("/hello")
    public ResponseEntity<String> getHello() throws JsonProcessingException {
        //Integer count = 121;
        String text = "Welcome to TTPL.... Best Software Development Company in Bhutan...!";
        return ResponseEntity.ok(mapper.writeValueAsString(text));
    }

    @GetMapping("/totalUserCount")
    public Long getRowCount(){
        Long count = service.totalCount();
        return count;
    }

    @GetMapping
    public List<SaUser> query(@RequestParam("limit") Integer size
            , @RequestParam("page") Integer page){
        //TODO: Test with RestExecutor
        List<SaUser> saUsers = service.findAll(page, size);
        return saUsers;
    }

    @PostMapping
    public SaUser insert(@Valid @RequestBody SaUser saUser){
        //TODO: Test with RestExecutor
        SaUser nSaUser = service.add(saUser);
        return nSaUser;
    }

    @PutMapping
    public SaUser update(@Valid @RequestBody SaUser saUser){
        //TODO: Test with RestExecutor
        SaUser old = service.update(saUser);
        return old;
    }

    @DeleteMapping
    public Boolean delete(@RequestParam("userid") Long userid){
        //TODO: Test with RestExecutor
        boolean deleted = service.remove(userid);
        return deleted;
    }

}
