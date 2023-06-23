package com.microservice.erp.domain.entities;

import lombok.*;

import javax.persistence.AttributeOverride;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import java.math.BigInteger;

@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "tms_reason")
@AttributeOverride(name = "id", column = @Column(name = "reason_id",columnDefinition = "bigint"))
public class Reason extends Auditable<BigInteger, Long> {

    @Basic(optional = false)
    @NotNull(message = "Reason name cannot be null")
    @Column(name = "reason_name", columnDefinition = "varchar(255)")
    private String reasonName;

    @Basic(optional = false)
    @NotNull(message = "Code cannot be null")
    @Column(name = "code", columnDefinition = "varchar(3)")
    private String code;

    @NotNull
    @Basic(optional = false)
    @Column(name = "defer_exempt", columnDefinition = "char(1)")
    private Character defermentExemption;

    @NotNull
    @Basic(optional = false)
    @Column(name = "is_medical_reason", columnDefinition = "char(1)")
    private Character isMedicalReason;

    @NotNull
    @Basic(optional = false)
    @Column(name = "status", columnDefinition = "char(1)")
    private Character status;


    public String getReasonName() {
        return reasonName;
    }

    public void setReasonName(String reasonName) {
        this.reasonName = reasonName;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Character getDefermentExemption() {
        return defermentExemption;
    }

    public Character getIsMedicalReason() {
        return isMedicalReason;
    }

    public void setIsMedicalReason(Character isMedicalReason) {
        this.isMedicalReason = isMedicalReason;
    }

    public void setDefermentExemption(Character defermentExemption) {
        this.defermentExemption = defermentExemption;
    }

    public Character getStatus() {
        return status;
    }

    public void setStatus(Character status) {
        this.status = status;
    }
}
