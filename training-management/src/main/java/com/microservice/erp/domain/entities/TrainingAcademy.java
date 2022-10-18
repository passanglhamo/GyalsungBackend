package com.microservice.erp.domain.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Setter
@Getter
@Entity(name = "st_training_academy")
public class TrainingAcademy {
    @Id
    @Column(name = "training_aca_id", columnDefinition = "int")
    private Integer trainingAcaId;

    @Column(name = "name", columnDefinition = "varchar(255)")
    private String name;
    //endregion

    public TrainingAcademy() {
    }

    public TrainingAcademy(Integer trainingAcaId, String name) {
        this.trainingAcaId = trainingAcaId;
        this.name = name;
    }
}
