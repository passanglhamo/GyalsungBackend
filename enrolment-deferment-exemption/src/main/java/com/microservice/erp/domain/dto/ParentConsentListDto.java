package com.microservice.erp.domain.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ParentConsentListDto {
    private Long userId;
    private String fullName;
    private String guardianName;
    private String guardianMobileNo;
    private String cid;
    private String dob;
}
