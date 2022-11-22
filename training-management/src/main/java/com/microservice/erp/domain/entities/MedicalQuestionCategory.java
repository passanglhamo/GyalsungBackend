package com.microservice.erp.domain.entities;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.AttributeOverride;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import java.math.BigInteger;

/**
 * @author Rajib Kumer Ghosh
 */
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "tms_medical_question_category")
@AttributeOverride(name = "id", column = @Column(name = "medical_question_category_id"))
public class MedicalQuestionCategory extends Auditable<BigInteger, Long> {

    @Basic(optional = false)
    @NotNull(message = "Category name cannot be null.")
    @Column(name = "category_name", columnDefinition = "varchar(255)")
    private String categoryName;

    @NotNull
    @Basic(optional = false)
    @Column(name = "status", columnDefinition = "char(1)")
    private String status;

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
