package com.microservice.erp.services.iServices;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.microservice.erp.domain.dto.UserDto;
import org.springframework.http.ResponseEntity;
import org.wso2.client.api.ApiException;

import java.io.IOException;
import java.text.ParseException;

public interface ISaUserService {

    ResponseEntity<?> getCensusDetailByCid(String cid) throws IOException, ParseException, ApiException;

    ResponseEntity<?> saveUser(UserDto userDto) throws IOException, ParseException, ApiException;

    ResponseEntity<?> getUsers(String authHeader);

    ResponseEntity<?> getOperatorUsers(String authHeader);
}
