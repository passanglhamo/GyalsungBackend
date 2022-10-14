package com.microservice.erp.domain.entities;

import javax.persistence.*;

@Entity(name = "signup_sms_otp")
public class SignupSmsOtp {
    //region private variables
    @Id
    @Column(name = "mobileNo", columnDefinition = "varchar(50)")
    private String mobileNo;

    @Column(name = "otp", columnDefinition = "varchar(4)")
    private String otp;
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
    //endregion
}
