package com.microservice.erp.domain.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigInteger;

@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "ede_parent_consent_otp")
public class ParentConsentOtp {

    //region private variables
    @Id
    @Column(name = "user_id", columnDefinition = "bigint")
    private BigInteger userId;

    @Basic(optional = false)
    @NotNull(message = "Parent/Guardian mobile number cannot not be null")
    @Column(name = "guardian_mobile_no", columnDefinition = "varchar(255)")
    private String guardianMobileNo;

    @Basic(optional = false)
    @NotNull(message = "OTP cannot not be null")
    @Column(name = "otp", columnDefinition = "varchar(255)")
    private String otp;
    //endregion

    //region setters and getters

    public BigInteger getUserId() {
        return userId;
    }

    public void setUserId(BigInteger userId) {
        this.userId = userId;
    }

    public String getGuardianMobileNo() {
        return guardianMobileNo;
    }

    public void setGuardianMobileNo(String guardianMobileNo) {
        this.guardianMobileNo = guardianMobileNo;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    //endregion
}
