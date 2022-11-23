package com.microservice.erp.domain.entities;

import lombok.*;

import javax.persistence.AttributeOverride;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import java.math.BigInteger;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "tms_field_specialization")
@AttributeOverride(name = "id", column = @Column(name = "field_specialization_id",columnDefinition = "bigint"))
public class FieldSpecialization extends Auditable<BigInteger, Long> {

    @Basic(optional = false)
    @NotNull(message = "Particular cannot be null")
    @Column(name = "field_spec_name",columnDefinition = "varchar(255)")
    private String fieldSpecName;

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

    public String getFieldSpecName() {
        return fieldSpecName;
    }

    public void setFieldSpecName(String fieldSpecName) {
        this.fieldSpecName = fieldSpecName;
    }

    public Boolean getMathRequired() {
        return mathRequired;
    }

    public void setMathRequired(Boolean mathRequired) {
        this.mathRequired = mathRequired;
    }

    public Boolean getDefaultCourse() {
        return defaultCourse;
    }

    public void setDefaultCourse(Boolean defaultCourse) {
        this.defaultCourse = defaultCourse;
    }

    public Character getStatus() {
        return status;
    }

    public void setStatus(Character status) {
        this.status = status;
    }
}
