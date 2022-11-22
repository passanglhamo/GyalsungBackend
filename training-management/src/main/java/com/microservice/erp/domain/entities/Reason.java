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
@AttributeOverride(name = "id", column = @Column(name = "reason_id"))
public class Reason extends Auditable<BigInteger, Long> {

    @Basic(optional = false)
    @NotNull(message = "Reason name cannot be null")
    @Column(name = "reason_name", columnDefinition = "varchar(255)")
    private String reasonName;

    @NotNull
    @Basic(optional = false)
    @Column(name = "status", columnDefinition = "char(1)")
    private String status;


    public String getReasonName() {
        return reasonName;
    }

    public void setReasonName(String reasonName) {
        this.reasonName = reasonName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
