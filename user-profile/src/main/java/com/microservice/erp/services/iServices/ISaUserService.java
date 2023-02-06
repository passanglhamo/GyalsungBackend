package com.microservice.erp.services.iServices;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.microservice.erp.domain.dto.UserDto;
import org.springframework.http.ResponseEntity;
import org.wso2.client.api.ApiException;

import java.io.IOException;
import java.text.ParseException;

public interface ISaUserService {

    ResponseEntity<?> getCensusDetailByCid(String cid) throws IOException, ParseException, ApiException;

    ResponseEntity<?> getAllRoles();

    ResponseEntity<?> saveUser(UserDto userDto) throws JsonProcessingException;

    ResponseEntity<?> getUsers(String authHeader);

}
