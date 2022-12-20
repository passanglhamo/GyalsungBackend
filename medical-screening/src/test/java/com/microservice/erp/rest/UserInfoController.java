/*
package com.microservice.erp.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservice.erp.domain.entities.UserInfo;
import com.microservice.erp.services.iServices.IUserInfoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

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
        Integer count = 121;
        return ResponseEntity.ok(mapper.writeValueAsString(count));
    }

    @GetMapping("/rowCount")
    public Long getRowCount(){
        Long count = service.totalCount();
        return count;
    }

    @GetMapping
    public List<UserInfo> query(@RequestParam("limit") Integer size
            , @RequestParam("page") Integer page){
        //TODO: Test with RestExecutor
        List<UserInfo> userInfos = service.findAll(page, size);
        return userInfos;
    }

    @PostMapping
    public UserInfo insert(@Valid @RequestBody UserInfo userInfo){
        //TODO: Test with RestExecutor
        UserInfo nUserInfo = service.add(userInfo);
        return nUserInfo;
    }

    @PutMapping
    public UserInfo update(@Valid @RequestBody UserInfo userInfo){
        //TODO: Test with RestExecutor
        UserInfo old = service.update(userInfo);
        return old;
    }

    @DeleteMapping
    public Boolean delete(@RequestParam("userid") Integer userid){
        //TODO: Test with RestExecutor
        boolean deleted = service.remove(userid);
        return deleted;
    }

}
*/
