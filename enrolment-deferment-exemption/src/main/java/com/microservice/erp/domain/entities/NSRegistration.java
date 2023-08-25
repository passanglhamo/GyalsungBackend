package com.microservice.erp.domain.entities;

import com.microservice.erp.domain.helper.BaseEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigInteger;
import java.util.Date;

@Entity(name = "ede_ns_registration")
public class NSRegistration extends BaseEntity {
    //region private variables
    @Id
    @Column(name = "registration_id", columnDefinition = "bigint")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private BigInteger registrationId;

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

    public BigInteger getRegistrationId() {
        return registrationId;
    }

    public void setRegistrationId(BigInteger registrationId) {
        this.registrationId = registrationId;
    }
}
