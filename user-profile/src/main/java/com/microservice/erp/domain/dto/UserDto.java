package com.microservice.erp.domain.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;
import java.util.List;

@Setter
@Getter
public class UserDto {

    private BigInteger userId;
    private String fullName;
    private Character gender;
    private String mobileNo;
    private String email;
    private String password;
    private Character status;

    private List<BigInteger> roles;
}
