package com.microservice.erp.domain.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;

@Setter
@Getter
public class ParentConsentDto {
    private BigInteger user_id;
    private BigInteger userId;
    private String mobileNo;
    private String fullName;
    private String email;
    private String guardianName;
    private String guardianMobileNo;
    private String otp;
    private String guardianEmail;
    private String relationToGuardian;
}
