package com.microservice.erp.domain.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigInteger;


@Entity(name = "change_email_verification_code")
public class ChangeEmailVerificationCode {
    //region private variables
    @Id
    @Column(name = "userId", columnDefinition = "bigint")
    private BigInteger userId;

    @Column(name = "email", columnDefinition = "varchar(255)")
    private String email;

    @Column(name = "verificationCode", columnDefinition = "varchar(6)")
    private String verificationCode;
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
    //endregion
}
