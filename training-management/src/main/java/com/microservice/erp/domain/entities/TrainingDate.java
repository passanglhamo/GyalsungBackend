package com.microservice.erp.domain.entities;


import com.microservice.erp.domain.helper.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigInteger;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "tms_training_date")
 public class TrainingDate extends BaseEntity {

    @Id
    @Column(name = "training_date_id", columnDefinition = "bigint")
    private BigInteger trainingDateId;

    @NotNull
    @Basic(optional = false)
    @Column(name = "training_date", columnDefinition = "date")
    private Date trainingDate;

    @NotNull
    @Basic(optional = false)
    @Column(name = "status", columnDefinition = "char(1)")
    private Character status;

    public BigInteger getTrainingDateId() {
        return trainingDateId;
    }

    public void setTrainingDateId(BigInteger trainingDateId) {
        this.trainingDateId = trainingDateId;
    }

    public Date getTrainingDate() {
        return trainingDate;
    }

    public void setTrainingDate(Date trainingDate) {
        this.trainingDate = trainingDate;
    }

    public Character getStatus() {
        return status;
    }

    public void setStatus(Character status) {
        this.status = status;
    }
}
