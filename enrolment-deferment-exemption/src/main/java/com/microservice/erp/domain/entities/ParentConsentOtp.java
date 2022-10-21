package com.microservice.erp.domain.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity(name = "ede_parent_consent_otp")
public class ParentConsentOtp {

    @Id
    @Column(name = "user_Id")
    private Long userId;

    @Basic(optional = false)
    @NotNull(message = "Parent/Guardian mobile number cannot not be null")
    @Column(name = "guardian_mobile_no", columnDefinition = "varchar(255)")
    private String guardianMobileNo;

    @Basic(optional = false)
    @NotNull(message = "OTP cannot not be null")
    @Column(name = "otp", columnDefinition = "varchar(255)")
    private String otp;
}
