package com.microservice.erp.domain.entities;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.AttributeOverride;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import java.math.BigInteger;
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "ede_dzongkhag_training_mapping")
@AttributeOverride(name = "id", column = @Column(name = "dzongkhag_hospital_training_id",columnDefinition = "bigint"))
public class DzongkhagTrainingMapping extends Auditable<BigInteger,Long>{

    @NotNull
    @Basic(optional = false)
    @Column(name = "dzongkhag_id",columnDefinition = "int")
    private Integer dzongkhagId;

    @NotNull
    @Basic(optional = false)
    @Column(name = "training_id",columnDefinition = "int")
    private Integer trainingId;

    @NotNull
    @Basic(optional = false)
    @Column(name = "status",columnDefinition = "char(1)")
    private String status;

    public Integer getDzongkhagId() {
        return dzongkhagId;
    }

    public void setDzongkhagId(Integer dzongkhagId) {
        this.dzongkhagId = dzongkhagId;
    }

    public Integer getTrainingId() {
        return trainingId;
    }

    public void setTrainingId(Integer trainingId) {
        this.trainingId = trainingId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
