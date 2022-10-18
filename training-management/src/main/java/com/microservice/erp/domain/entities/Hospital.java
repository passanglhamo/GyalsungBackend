package com.microservice.erp.domain.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Setter
@Getter
@Entity(name = "st_hospital")
public class Hospital {
    @Id
    @Column(name = "hospital_id", columnDefinition = "int")
    private Integer hospitalId;

    @Column(name = "hospital_name", columnDefinition = "varchar(255)")
    private String hospitalName;

    @Column(name = "status", columnDefinition = "char(1)")
    private Character status;


    public Hospital() {
    }

    public Hospital(Integer hospitalId, String hospitalName) {
        this.hospitalId = hospitalId;
        this.hospitalName = hospitalName;
    }
}
