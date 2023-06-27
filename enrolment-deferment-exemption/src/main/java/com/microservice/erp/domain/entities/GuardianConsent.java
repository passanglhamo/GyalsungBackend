package com.microservice.erp.domain.entities;

import com.microservice.erp.domain.helper.BaseEntity;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.math.BigInteger;
import java.util.Date;

@Entity(name = "ede_guardian_consent")
public class GuardianConsent extends BaseEntity {

    //region private variables
    @Id
    @Column(name = "consent_id", columnDefinition = "bigint")
    private BigInteger consentId;

    @NotNull(message = "User id cannot be null")
    @Column(name = "user_id", columnDefinition = "bigint")
    private BigInteger userId;

    @Column(name = "full_name", columnDefinition = "varchar(255)")
    private String fullName;

    @Column(name = "email", columnDefinition = "varchar(255)")
    private String email;

    @Column(name = "mobile_no", columnDefinition = "varchar(255)")
    private String mobileNo;

    @Column(name = "guardian_name", columnDefinition = "varchar(255)")
    private String guardianName;

    @Column(name = "guardian_cid", columnDefinition = "varchar(255)")
    private String guardianCid;

    @Column(name = "guardian_mobile_no", columnDefinition = "varchar(255)")
    private String guardianMobileNo;

    @Column(name = "guardian_email", columnDefinition = "varchar(255)")
    private String guardianEmail;

    @Column(name = "relation_to_guardian", columnDefinition = "varchar(255)")
    private String relationToGuardian;

    @NotNull(message = "Request date cannot be null")
    @Column(name = "consent_request_date")
    private Date consentRequestDate;

    @Column(name = "consent_date")//date entered when guardian consent or deny
    private Date consentDate;

    @NotNull
    @Column(name = "status", columnDefinition = "char(1)")
    private Character status;
    //endregion


    //region setters and getters

    public BigInteger getConsentId() {
        return consentId;
    }

    public void setConsentId(BigInteger consentId) {
        this.consentId = consentId;
    }

    public BigInteger getUserId() {
        return userId;
    }

    public void setUserId(BigInteger userId) {
        this.userId = userId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getGuardianName() {
        return guardianName;
    }

    public void setGuardianName(String guardianName) {
        this.guardianName = guardianName;
    }

    public String getGuardianCid() {
        return guardianCid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public void setGuardianCid(String guardianCid) {
        this.guardianCid = guardianCid;
    }

    public String getGuardianMobileNo() {
        return guardianMobileNo;
    }

    public void setGuardianMobileNo(String guardianMobileNo) {
        this.guardianMobileNo = guardianMobileNo;
    }

    public String getGuardianEmail() {
        return guardianEmail;
    }

    public void setGuardianEmail(String guardianEmail) {
        this.guardianEmail = guardianEmail;
    }

    public String getRelationToGuardian() {
        return relationToGuardian;
    }

    public void setRelationToGuardian(String relationToGuardian) {
        this.relationToGuardian = relationToGuardian;
    }

    public Date getConsentRequestDate() {
        return consentRequestDate;
    }

    public void setConsentRequestDate(Date consentRequestDate) {
        this.consentRequestDate = consentRequestDate;
    }

    public Date getConsentDate() {
        return consentDate;
    }

    public void setConsentDate(Date consentDate) {
        this.consentDate = consentDate;
    }

    public Character getStatus() {
        return status;
    }

    public void setStatus(Character status) {
        this.status = status;
    }

    //endregion
}
