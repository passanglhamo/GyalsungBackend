package com.microservice.erp.domain.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
public class SignupRequestDto {
    private String fullName;
    private String sex;
    private String cid;
    private String studentCode;
    private String birthDate;
    private Date dob;
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
 }
