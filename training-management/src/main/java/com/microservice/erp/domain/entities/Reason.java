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
@Entity(name = "tms_reason")
@AttributeOverride(name = "id", column = @Column(name = "reason_id"))
public class Reason extends Auditable<Long,Long>{

    @Basic(optional = false)
    @NotNull(message = "Name cannot be null")
    @Column(name = "name",columnDefinition = "varchar(255)")
    private String name;

    @NotNull
    @Basic(optional = false)
    @Column(name = "status",columnDefinition = "char(1)")
    private Character status;
}
