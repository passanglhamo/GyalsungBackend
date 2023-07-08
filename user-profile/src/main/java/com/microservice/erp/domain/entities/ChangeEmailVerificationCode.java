package com.microservice.erp.domain.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigInteger;
import java.util.Date;


@Entity(name = "change_email_verification_code")
public class ChangeEmailVerificationCode {
    //region private variables
    @Id
    @Column(name = "user_id", columnDefinition = "bigint")
    private BigInteger userId;

    @Column(name = "email", columnDefinition = "varchar(255)")
    private String email;

    @Column(name = "verification_code", columnDefinition = "varchar(6)")
    private String verificationCode;

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
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
