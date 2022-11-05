package com.microservice.erp.domain.dto.parentConsent;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ParentConsentDto {
    private Long userId;
    private String mobileNo;
    private String fullName;
    private String guardianName;
    private String guardianMobileNo;
    private String otp;
    private String guardianEmail;
    private String relationToGuardian;
}
