package com.microservice.erp.domain.entities;

import lombok.*;

import javax.persistence.AttributeOverride;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import java.util.Objects;

/**
 * @author Rajib Kumer Ghosh
 */

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@EqualsAndHashCode
@Entity(name = "tms_field_specialization")
@AttributeOverride(name = "id", column = @Column(name = "field_specialization_id"))
public class FieldSpecialization extends Auditable<Long, Long> {

    @Basic(optional = false)
    @NotNull(message = "Name cannot be null")
    @Column(name = "name",columnDefinition = "varchar(255)")
    private String name;

    @Basic(optional = false)
    @Column(name = "math_required")
    private Boolean mathRequired;

    @Basic(optional = false)
    @Column(name = "default_course")
    private Boolean defaultCourse;

    @NotNull
    @Basic(optional = false)
    @Column(name = "status",columnDefinition = "char(1)")
    private Character status;

}
