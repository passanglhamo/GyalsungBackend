package com.microservice.erp.domain.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;


@Entity(name = "st_training_academy")
public class TrainingAcademy {
    @Id
    @Column(name = "training_aca_id", columnDefinition = "int")
    private Integer trainingAcaId;

    @Column(name = "name", columnDefinition = "varchar(255)")
    private String name;
    @Column(name = "battalion", columnDefinition = "int")
    private String battalion;
    //endregion

    //region setters and getters
    public Integer getTrainingAcaId() {
        return trainingAcaId;
    }

    public void setTrainingAcaId(Integer trainingAcaId) {
        this.trainingAcaId = trainingAcaId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBattalion() {
        return battalion;
    }

    public void setBattalion(String battalion) {
        this.battalion = battalion;
    }
    //endregion
}
