package com.microservice.erp.domain.entities;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigInteger;

@Setter
@Getter
@Entity(name = "med_medical_self_declaration")
@AttributeOverride(name = "id", column = @Column(name = "self_declaration_id", columnDefinition = "bigint"))
public class MedicalSelfDeclaration extends Auditable<BigInteger, Long> {

    //region private variables
    @NotNull
    @Column(name = "user_id", columnDefinition = "bigint")
    private BigInteger userId;

    @NotNull
    @Column(name = "medical_questionnaire_id", columnDefinition = "bigint")
    private BigInteger medicalQuestionnaireId;

    @NotNull(message = "Medical Questionnaire cannot be null.")
    @Column(name = "medical_question_name", columnDefinition = "varchar(255)")
    private String medicalQuestionName;

    @NotNull()
    @Column(name = "check_status", columnDefinition = "char(1)")
    private Character checkStatus;
    //endregion

}
