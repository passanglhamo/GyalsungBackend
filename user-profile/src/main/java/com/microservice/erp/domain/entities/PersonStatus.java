package com.microservice.erp.domain.entities;

import javax.persistence.*;
import java.math.BigInteger;

@Entity(name = "st_person_status")
public class PersonStatus {
    //region private variables

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", columnDefinition = "bigint")
    private BigInteger id;

    @Column(name = "person_status", columnDefinition = "varchar(255)")
    private String personStatus;

    @Column(name = "is_student", columnDefinition = "char(1)")
    private Character isStudent;
    //endregion


    //region getter and setter
    public BigInteger getId() {
        return id;
    }

    public void setId(BigInteger id) {
        this.id = id;
    }

    public String getPersonStatus() {
        return personStatus;
    }

    public void setPersonStatus(String personStatus) {
        this.personStatus = personStatus;
    }

    public Character getIsStudent() {
        return isStudent;
    }

    public void setIsStudent(Character isStudent) {
        this.isStudent = isStudent;
    }

    //endregion
}
