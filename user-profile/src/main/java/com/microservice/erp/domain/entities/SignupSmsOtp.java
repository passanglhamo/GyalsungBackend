package com.microservice.erp.domain.entities;

import javax.persistence.*;
import java.util.Date;

@Entity(name = "signup_sms_otp")
public class SignupSmsOtp {
    //region private variables
    @Id
    @Column(name = "mobile_no", columnDefinition = "varchar(50)")
    private String mobileNo;

    @Column(name = "otp", columnDefinition = "varchar(6)")
    private String otp;

    @Column(name = "date")
    private Date date;

    @Column(name = "expiry_time")//180 seconds
    private Integer expiryTime;
    //endregion

    //region setters and getters

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
