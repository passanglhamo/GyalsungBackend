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
@Entity(name = "tms_medical_question_sub_category")
@AttributeOverride(name = "id", column = @Column(name = "medical_question_sub_category_id",columnDefinition = "bigint"))
public class MedicalQuestionSubCategory extends Auditable<BigInteger, Long> {

    @Basic(optional = false)
    @NotNull(message = "Name cannot be null.")
    @Column(name = "sub_category_name",columnDefinition = "varchar(255)")
    private String name;

    @NotNull
    @Basic(optional = false)
    @Column(name = "medical_question_category_id",columnDefinition = "bigint")
    private BigInteger categoryID;

    @NotNull
    @Basic(optional = false)
    @Column(name = "status",columnDefinition = "char(1)")
    private String status;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigInteger getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(BigInteger categoryID) {
        this.categoryID = categoryID;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
