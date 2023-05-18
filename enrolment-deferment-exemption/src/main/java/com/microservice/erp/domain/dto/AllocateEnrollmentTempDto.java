package com.microservice.erp.domain.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;

@Setter
@Getter
public class AllocateEnrollmentTempDto {
    private BigInteger userId;
    private Integer academyId;
    private String mobileNo;
    private String fullName;
    private String email;
    private String academyName;
}
