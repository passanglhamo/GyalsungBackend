package com.microservice.erp.domain.entities;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigInteger;
import java.util.Date;
import java.util.Set;

/**
 * @author Rajib Kumer Ghosh
 */

@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
@Entity(name = "ede_exemption_info")
@AttributeOverride(name = "id", column = @Column(name = "exemption_id"))
public class ExemptionInfo extends Auditable<BigInteger, Long> {

    @NotNull(message = "User id cannot be null")
    @Basic(optional = false)
    @Column(name = "user_id")
    private BigInteger userId;

    @NotNull(message = "Application date cannot be null")
    @Basic(optional = false)
    @Column(name = "application_date")
    @Temporal(TemporalType.DATE)
    private Date applicationDate = new java.sql.Date(new Date().getTime());


    @Basic(optional = false)
    @NotNull(message = "Reasons cannot be null")
    @Column(name = "reason_id",columnDefinition = "varchar(255)")
    private BigInteger reasonId;

    @Column(name = "approval_remarks",columnDefinition = "varchar(255)")
    private String approvalRemarks;


    @Column(name = "remarks",columnDefinition = "varchar(255)")
    private String remarks;

    @NotNull
    @Basic(optional = false)
    @Column(name = "status",columnDefinition = "char(1)")
    private Character status;

    @OneToMany(
            mappedBy = "exemption",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    private Set<ExemptionFileInfo> files;

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
}
