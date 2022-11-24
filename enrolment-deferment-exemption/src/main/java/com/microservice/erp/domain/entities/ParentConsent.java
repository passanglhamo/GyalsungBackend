package com.microservice.erp.domain.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.AttributeOverride;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import java.math.BigInteger;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "ede_parent_consent")
@AttributeOverride(name = "id", column = @Column(name = "parent_consent_id", columnDefinition = "bigint"))
public class ParentConsent extends Auditable<BigInteger, Long> {

    //region private variables
    @Basic(optional = false)
    @NotNull(message = "User Id not be null")
    @Column(name = "user_id", columnDefinition = "bigint")
    private BigInteger userId;

    @NotNull(message = "Year cannot not be null")
    @Column(name = "year", columnDefinition = "varchar(4)")
    private String year;

    @Column(name = "submitted_on")
    private Date submittedOn;
    @Basic(optional = false)
    @NotNull(message = "Parent/Guardian name not be null")
    @Column(name = "guardian_name", columnDefinition = "varchar(255)")
    private String guardianName;

    @Basic(optional = false)
    @NotNull(message = "Parent/Guardian mobile number cannot not be null")
    @Column(name = "guardian_mobile_no", columnDefinition = "varchar(255)")
    private String guardianMobileNo;
    //endregion

    //region setters and getters

    public BigInteger getUserId() {
        return userId;
    }

    public void setUserId(BigInteger userId) {
        this.userId = userId;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public Date getSubmittedOn() {
        return submittedOn;
    }

    public void setSubmittedOn(Date submittedOn) {
        this.submittedOn = submittedOn;
    }

    public String getGuardianName() {
        return guardianName;
    }

    public void setGuardianName(String guardianName) {
        this.guardianName = guardianName;
    }

    public String getGuardianMobileNo() {
        return guardianMobileNo;
    }

    public void setGuardianMobileNo(String guardianMobileNo) {
        this.guardianMobileNo = guardianMobileNo;
    }

    // endregion
}
