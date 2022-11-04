package com.microservice.erp.domain.entities;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Setter
@Getter
@Entity(name = "med_medical_self_declaration")
@AttributeOverride(name = "id", column = @Column(name = "self_declaration_id"))
public class MedicalSelfDeclaration extends Auditable<Long, Long> {

    //region private variables
    @NotNull
    @Column(name = "user_id")
    private Long userId;

    @NotNull
    @Column(name = "medical_questionnaire_id")
    private Long medicalQuestionnaireId;

    @NotNull(message = "Medical Questionnaire cannot be null.")
    @Column(name = "medical_question_name", columnDefinition = "varchar(255)")
    private String medicalQuestionName;

    @NotNull()
    @Column(name = "check_status", columnDefinition = "char(1)")
    private Character checkStatus;
    //endregion

}
