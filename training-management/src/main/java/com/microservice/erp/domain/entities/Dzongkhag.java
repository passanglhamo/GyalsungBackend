package com.microservice.erp.domain.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Setter
@Getter
@Entity(name = "st_dzongkhag")
public class Dzongkhag {

    @Id
    @Column(name = "dzongkhag_id", columnDefinition = "int")
    private Integer dzongkhagId;

    @Column(name = "dzongkhag_name", columnDefinition = "varchar(255)")
    private String dzongkhagName;


    public Dzongkhag() {
    }
    public Dzongkhag(Integer dzongkhagId, String dzongkhagName) {
        this.dzongkhagId = dzongkhagId;
        this.dzongkhagName = dzongkhagName;
    }


}
