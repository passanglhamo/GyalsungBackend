package com.microservice.erp.domain.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;
import java.util.Date;

@Setter
@Getter
public class ParentConsentListDto {
    private BigInteger userId;
    private String fullName;
    private String guardianName;
    private String guardianMobileNo;
    private String cid;
    private String dob;
    private Date submittedOn;
}
