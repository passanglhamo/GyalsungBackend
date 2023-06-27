package com.microservice.erp.domain.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigInteger;
import java.util.Date;

@Entity(name = "ede_guardian_consent_otp")
public class GuardianConsentOtp {
    //region private variables
    @Id
    @Column(name = "otp_id", columnDefinition = "bigint")
    private BigInteger otpId;

    @Column(name = "mobile_no", columnDefinition = "varchar(50)")
    private String mobileNo;

    @Column(name = "guardian_cid", columnDefinition = "varchar(50)")
    private String guardianCid;

    @Column(name = "otp", columnDefinition = "varchar(6)")
    private String otp;

    @Column(name = "date")
    private Date date;

    @Column(name = "expiry_time")//180 seconds
    private Integer expiryTime;
    //endregion

    //region setters and getters

    public BigInteger getOtpId() {
        return otpId;
    }

    public void setOtpId(BigInteger otpId) {
        this.otpId = otpId;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getGuardianCid() {
        return guardianCid;
    }

    public void setGuardianCid(String guardianCid) {
        this.guardianCid = guardianCid;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Integer getExpiryTime() {
        return expiryTime;
    }

    public void setExpiryTime(Integer expiryTime) {
        this.expiryTime = expiryTime;
    }

    //endregion
}
