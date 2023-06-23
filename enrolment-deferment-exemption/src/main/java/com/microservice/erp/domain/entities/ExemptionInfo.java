package com.microservice.erp.domain.entities;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
@Entity(name = "ede_exemption_info")
@AttributeOverride(name = "id", column = @Column(name = "exemption_id", columnDefinition = "bigint"))
public class ExemptionInfo extends Auditable<BigInteger, Long> {

    //region private variables
    @NotNull(message = "User id cannot be null")
    @Basic(optional = false)
    @Column(name = "user_id", columnDefinition = "bigint")
    private BigInteger userId;

    @NotNull
    @Basic(optional = false)
    @Column(name = "case_number", columnDefinition = "varchar(20) ")
    private String caseNumber;

    @CreatedDate
    @Column(name = "application_date")
    private Date applicationDate;

    @Basic(optional = false)
    @NotNull(message = "Reason id cannot be null")
    @Column(name = "reason_id", columnDefinition = "bigint")
    private BigInteger reasonId;

    @Column(name = "approval_remarks", columnDefinition = "varchar(255)")
    private String approvalRemarks;


    @Column(name = "remarks", columnDefinition = "varchar(255)")
    private String remarks;

    @NotNull
    @Basic(optional = false)
    @Column(name = "status", columnDefinition = "char(1)")
    private Character status;

    @NotNull
    @Basic(optional = false)
    @Column(name = "exemption_year", columnDefinition = "char(4)")
    private String exemptionYear;

    @Column(name = "gender", columnDefinition = "char(1)")
    private Character gender;

    @OneToMany(
            mappedBy = "exemption",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    private Set<ExemptionFileInfo> files;
    //endregion

    //region setters and getters
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

    public BigInteger getReasonId() {
        return reasonId;
    }

    public void setReasonId(BigInteger reasonId) {
        this.reasonId = reasonId;
    }

    public String getApprovalRemarks() {
        return approvalRemarks;
    }

    public void setApprovalRemarks(String approvalRemarks) {
        this.approvalRemarks = approvalRemarks;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Character getStatus() {
        return status;
    }

    public void setStatus(Character status) {
        this.status = status;
    }

    public Set<ExemptionFileInfo> getFiles() {
        return files;
    }

    public void setFiles(Set<ExemptionFileInfo> files) {
        this.files = files;
    }

    public String getExemptionYear() {
        return exemptionYear;
    }

    public void setExemptionYear(String exemptionYear) {
        this.exemptionYear = exemptionYear;
    }

    public Character getGender() {
        return gender;
    }

    public void setGender(Character gender) {
        this.gender = gender;
    }

    public String getCaseNumber() {
        return caseNumber;
    }

    public void setCaseNumber(String caseNumber) {
        this.caseNumber = caseNumber;
    }

    //endregion
}
