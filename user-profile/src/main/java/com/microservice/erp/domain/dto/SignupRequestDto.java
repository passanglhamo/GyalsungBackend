package com.microservice.erp.domain.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;
import java.util.Date;

@Setter
@Getter
public class SignupRequestDto {
    //region private variables
    private String fullName;
    private Character gender;
    private String cid;
    private String birthDate;
//    private Date dob;
    private String mobileNo;
    private String otp;
    private String email;
    private String verificationCode;
    private String password;
    private String confirmPassword;
    private String fatherName;
    private String fatherCid;
    private String motherName;
    private String motherCid;
    private String permanentCountry;
    private String permanentDzongkhag;
    private String permanentGeog;
    private String permanentPlaceName;
    private String presentCountry;
    private String guardianNameFirst;
    private String guardianCidFirst;
    private String guardianNameSecond;
    private String guardianCidSecond;
    //endregion
}
