package com.microservice.erp.domain.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;

@Setter
@Getter
public class MedicalQuestionDto {
    private BigInteger medicalQuestionId;
    private String medicalQuestionName;
    private Character checkStatus;
}
