package com.microservice.erp.services.iServices;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.microservice.erp.domain.dto.UserDto;
import org.springframework.http.ResponseEntity;

public interface ISaUserService {
    ResponseEntity<?> saveUser(UserDto userDto) throws JsonProcessingException;

    ResponseEntity<?> getUsers(String authHeader);
}
