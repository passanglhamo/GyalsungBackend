package com.microservice.erp.domain.entities;


import lombok.*;

import javax.persistence.AttributeOverride;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import java.math.BigInteger;

/**
 * @author Rajib Kumer Ghosh
 */

@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "tms_training_academy_capacity")
@AttributeOverride(name = "id", column = @Column(name = "training_academy_capacity_id"))
public class TrainingAcademyCapacity extends Auditable<BigInteger, Long> {

    @Basic(optional = false)
    @NotNull(message = "Name must not be null")
    @Column(name = "academy_id")
    private Integer academyId;

    @NotNull
    @Basic(optional = false)
    @Column(name = "training_year",columnDefinition = "char(4)")
    private String trainingYear;

    @Basic(optional = false)
    @NotNull(message = "Capacity must not be null")
    @Column(name = "male_capacity_amount")
    private Integer maleCapacityAmount;

    @Basic(optional = false)
    @NotNull(message = "Capacity must not be null")
    @Column(name = "female_capacity_amount")
    private Integer femaleCapacityAmount;


    @NotNull
    @Basic(optional = false)
    @Column(name = "status")
    private String status;

    public Integer getAcademyId() {
        return academyId;
    }

    public void setAcademyId(Integer academyId) {
        this.academyId = academyId;
    }

    public String getTrainingYear() {
        return trainingYear;
    }

    public void setTrainingYear(String trainingYear) {
        this.trainingYear = trainingYear;
    }

    public Integer getMaleCapacityAmount() {
        return maleCapacityAmount;
    }

    public void setMaleCapacityAmount(Integer maleCapacityAmount) {
        this.maleCapacityAmount = maleCapacityAmount;
    }

    public Integer getFemaleCapacityAmount() {
        return femaleCapacityAmount;
    }

    public void setFemaleCapacityAmount(Integer femaleCapacityAmount) {
        this.femaleCapacityAmount = femaleCapacityAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
