package com.microservice.erp.domain.entities;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import java.math.BigInteger;
import java.util.Date;

@Entity(name = "ede_ns_registration")
@AttributeOverride(name = "id", column = @Column(name = "registration_id", columnDefinition = "bigint"))
public class NSRegistration extends Auditable<BigInteger, Long> {
    //region private variables
    @NotNull(message = "User id cannot be null")
    @Column(name = "user_id", columnDefinition = "bigint")
    private BigInteger userId;

    @NotNull(message = "Gender cannot be null")
    @Column(name = "gender", columnDefinition = "char(1)")
    private Character gender;

    @NotNull(message = "Year cannot be null")
    @Column(name = "year", columnDefinition = "char(4)")
    private String year;

    @NotNull(message = "Enrolled on cannot be null")
    @Column(name = "registration_on")
    private Date registrationOn;

    @NotNull
    @Column(name = "status", columnDefinition = "char(1)")
    private Character status;

    public BigInteger getUserId() {
        return userId;
    }

    public void setUserId(BigInteger userId) {
        this.userId = userId;
    }

    public Character getGender() {
        return gender;
    }

    public void setGender(Character gender) {
        this.gender = gender;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public Date getRegistrationOn() {
        return registrationOn;
    }

    public void setRegistrationOn(Date registrationOn) {
        this.registrationOn = registrationOn;
    }

    public Character getStatus() {
        return status;
    }

    public void setStatus(Character status) {
        this.status = status;
    }
}
