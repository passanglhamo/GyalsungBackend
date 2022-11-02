package com.microservice.erp.domain.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class MedicalQuestionDto {
    private Long medicalQuestionId;
    private Character checkStatus;
}
