package com.microservice.erp.services.iServices;

import com.microservice.erp.domain.dto.UserDto;
import org.springframework.http.ResponseEntity;

public interface ISaUserService {
    ResponseEntity<?> getAllRoles();

    ResponseEntity<?> addUser(UserDto userDto);

}
