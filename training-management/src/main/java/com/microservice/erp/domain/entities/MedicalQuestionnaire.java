package com.microservice.erp.domain.entities;

import lombok.*;

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
@Entity(name = "tms_medical_questionnaire")
@AttributeOverride(name = "id", column = @Column(name = "medical_questionnaire_id"))
public class MedicalQuestionnaire extends Auditable<BigInteger, Long> {

    @Basic(optional = false)
    @NotNull(message = "Medical Questionnaire cannot be null.")
    @Column(name = "medical_question_name",columnDefinition = "varchar(255)")
    private String medicalQuestionName;

    @NotNull
    @Basic(optional = false)
    @Column(name = "medical_question_category_id")
    private Long categoryId;

    @NotNull
    @Basic(optional = false)
    @Column(name = "status",columnDefinition = "char(1)")
    private String status;

    public String getMedicalQuestionName() {
        return medicalQuestionName;
    }

    public void setMedicalQuestionName(String medicalQuestionName) {
        this.medicalQuestionName = medicalQuestionName;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


}
