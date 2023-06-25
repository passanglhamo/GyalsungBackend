package com.microservice.erp.domain.entities;

import com.microservice.erp.domain.helper.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.math.BigInteger;
import java.util.Date;

@Entity(name = "ede_early_enlistment")
 public class EarlyEnlistment extends BaseEntity {

    //region private variables
    @Id
    @Column(name = "enlistment_id", columnDefinition = "bigint")
    private BigInteger enlistmentId;

    @NotNull(message = "User id cannot be null")
    @Column(name = "user_id", columnDefinition = "bigint")
    private BigInteger userId;

    @NotNull(message = "Application date cannot be null")
    @Column(name = "application_date")
    private Date applicationDate;

    @NotNull
    @Column(name = "status", columnDefinition = "char(1)")
    private Character status;
    //endregion


    //region setters and getters

    public BigInteger getEnlistmentId() {
        return enlistmentId;
    }

    public void setEnlistmentId(BigInteger enlistmentId) {
        this.enlistmentId = enlistmentId;
    }

    public BigInteger getUserId() {
        return userId;
    }

    public void setUserId(BigInteger userId) {
        this.userId = userId;
    }

    public Date getApplicationDate() {
        return applicationDate;
    }

    public void setApplicationDate(Date applicationDate) {
        this.applicationDate = applicationDate;
    }

    public Character getStatus() {
        return status;
    }

    public void setStatus(Character status) {
        this.status = status;
    }

    //endregion
}
