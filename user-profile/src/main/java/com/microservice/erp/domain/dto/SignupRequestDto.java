package com.microservice.erp.domain.dto;

import com.infoworks.lab.rest.validation.Password.PasswordRule;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
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
    @PasswordRule(mixLengthRule = 8, maxLengthRule = 20)
    @NotEmpty(message = "Password must not null or empty!")
    private String password;
    @PasswordRule(mixLengthRule = 8, maxLengthRule = 20)
    @NotEmpty(message = "Password must not null or empty!")
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
