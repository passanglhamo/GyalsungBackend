package com.microservice.erp.domain.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigInteger;
import java.util.Date;


@Entity(name = "change_mobile_no_sms_otp")
public class ChangeMobileNoSmsOtp {
    //region private variables
    @Id
    @Column(name = "user_id", columnDefinition = "bigint")
    private BigInteger userId;

    @Column(name = "mobile_no", columnDefinition = "varchar(50)")
    private String mobileNo;

    @Column(name = "otp", columnDefinition = "varchar(4)")
    private String otp;

    @Column(name = "date")
    private Date date;

    @Column(name = "expiry_time")//180 seconds
    private Integer expiryTime;
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
