package com.microservice.erp.domain.entities;

import com.microservice.erp.domain.helper.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigInteger;
import java.util.Date;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "ede_deferment_info")
public class DefermentInfo extends BaseEntity {

    //region private variables
    @Id
    @Column(name = "deferment_id", columnDefinition = "bigint")
    private BigInteger defermentId;

    @NotNull(message = "User id cannot be null")
    @Basic(optional = false)
    @Column(name = "user_id", columnDefinition = "bigint")
    private BigInteger userId;

    @NotNull
    @Basic(optional = false)
    @Column(name = "case_number", columnDefinition = "varchar(20) ")
    private String caseNumber;

    @Column(name = "gender", columnDefinition = "char(1)")
    private Character gender;

    @NotNull
    @Basic(optional = false)
    @Column(name = "deferment_year", columnDefinition = "char(4) ")
    private String defermentYear;

    @NotNull
    @Column(name = "application_date")
    private Date applicationDate;

    @Basic(optional = false)
    @NotNull(message = "Reasons cannot be null")
    @Column(name = "reason_id", columnDefinition = "bigint")
    private BigInteger reasonId;

    @Column(name = "reviewer_remarks", columnDefinition = "varchar(255)")
    private String reviewerRemarks;

    @Column(name = "approval_remarks", columnDefinition = "varchar(255)")
    private String approvalRemarks;

    @Column(name = "remarks", columnDefinition = "varchar(255)")
    private String remarks;

    @NotNull
    @Basic(optional = false)
    @Column(name = "status", columnDefinition = "char(1)")
    private Character status;

    @Column(name = "mail_status", columnDefinition = "char(1)")
    private Character mailStatus;

    @Column(name = "reviewer_id", columnDefinition = "bigint")
    private BigInteger reviewerId;

    @Column(name = "approver_id", columnDefinition = "bigint")
    private BigInteger approverId;

    @OneToMany(
            mappedBy = "deferment",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    private Set<DefermentFileInfo> files;
    //endregion


    public String getReviewerRemarks() {
        return reviewerRemarks;
    }

    public void setReviewerRemarks(String reviewerRemarks) {
        this.reviewerRemarks = reviewerRemarks;
    }

    public Character getMailStatus() {
        return mailStatus;
    }

    public void setMailStatus(Character mailStatus) {
        this.mailStatus = mailStatus;
    }

    public BigInteger getDefermentId() {
        return defermentId;
    }

    public void setDefermentId(BigInteger defermentId) {
        this.defermentId = defermentId;
    }

    public String getCaseNumber() {
        return caseNumber;
    }

    public void setCaseNumber(String caseNumber) {
        this.caseNumber = caseNumber;
    }

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

    public String getDefermentYear() {
        return defermentYear;
    }

    public void setDefermentYear(String defermentYear) {
        this.defermentYear = defermentYear;
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

    public Set<DefermentFileInfo> getFiles() {
        return files;
    }

    public void setFiles(Set<DefermentFileInfo> files) {
        this.files = files;
    }

    public BigInteger getReviewerId() {
        return reviewerId;
    }

    public void setReviewerId(BigInteger reviewerId) {
        this.reviewerId = reviewerId;
    }

    public BigInteger getApproverId() {
        return approverId;
    }

    public void setApproverId(BigInteger approverId) {
        this.approverId = approverId;
    }
}
