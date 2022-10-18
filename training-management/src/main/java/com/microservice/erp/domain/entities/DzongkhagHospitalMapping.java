package com.microservice.erp.domain.entities;

import lombok.*;

import javax.persistence.AttributeOverride;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@EqualsAndHashCode
@Entity(name = "tms_dzongkhag_hospital_mapping")
@AttributeOverride(name = "id", column = @Column(name = "dzongkhag_hospital_mapping_id"))
public class DzongkhagHospitalMapping extends Auditable<Long,Long>{

    @NotNull
    @Basic(optional = false)
    @Column(name = "dzongkhag_id")
    private Integer dzongkhagId;

    @NotNull
    @Basic(optional = false)
    @Column(name = "hospital_id")
    private Integer hospitalId;

    @NotNull
    @Basic(optional = false)
    @Column(name = "status",columnDefinition = "char(1)")
    private String status;
}
