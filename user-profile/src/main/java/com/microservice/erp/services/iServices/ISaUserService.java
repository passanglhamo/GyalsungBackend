package com.microservice.erp.services.iServices;

import com.microservice.erp.domain.dto.UserDto;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.text.ParseException;

public interface ISaUserService {

    ResponseEntity<?> getCensusDetailByCid(String cid) throws IOException, ParseException;

    ResponseEntity<?> saveUser(UserDto userDto) throws IOException, ParseException;

    ResponseEntity<?> getUsers(String authHeader);

    ResponseEntity<?> getOfficersByUserType(String authHeader, Character userTypeVal);


}
