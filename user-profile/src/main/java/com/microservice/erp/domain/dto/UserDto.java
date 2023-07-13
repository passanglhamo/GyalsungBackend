package com.microservice.erp.domain.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

@Setter
@Getter
public class UserDto {

    private BigInteger userId;
    private String cid;
    private String fullName;
    private String dateOfBirth;
    private Character gender;
    private String mobileNo;
    private String email;
    private String password;
    private String confirmPassword;
    private Character status;

    private List<BigInteger> roles;
}
