package com.microservice.erp.domain.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigInteger;


@Entity(name = "change_mobile_no_sms_otp")
public class ChangeMobileNoSmsOtp {
    //region private variables
    @Id
    @Column(name = "user_id")
    private BigInteger userId;

    @Column(name = "mobile_no", columnDefinition = "varchar(50)")
    private String mobileNo;

    @Column(name = "otp", columnDefinition = "varchar(4)")
    private String otp;
    //endregion

    //region setters and getters
    public BigInteger getUserId() {
        return userId;
    }

    public void setUserId(BigInteger userId) {
        this.userId = userId;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }
    //endregion
}
