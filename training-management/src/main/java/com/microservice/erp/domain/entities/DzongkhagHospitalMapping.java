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
@Entity(name = "tms_dzongkhag_hospital_mapping")
@AttributeOverride(name = "id", column = @Column(name = "dzongkhag_hospital_mapping_id",columnDefinition = "bigint"))
public class DzongkhagHospitalMapping extends Auditable<BigInteger,Long>{

    @NotNull
    @Basic(optional = false)
    @Column(name = "dzongkhag_id",columnDefinition = "int")
    private Integer dzongkhagId;

    @NotNull
    @Basic(optional = false)
    @Column(name = "hospital_id",columnDefinition = "int")
    private Integer hospitalId;

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

    public Integer getHospitalId() {
        return hospitalId;
    }

    public void setHospitalId(Integer hospitalId) {
        this.hospitalId = hospitalId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
