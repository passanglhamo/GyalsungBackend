package com.microservice.erp.domain.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;

@Setter
@Getter
public class TrainingAcademyWiseEnrolmentDto {
    //region private variables
    private Integer trainingAcaId;
    private String trainingAcademyName;
    private String battalion;
    private Integer noOfMaleEnrolled;
    private Integer noOfFemaleEnrolled;
    //endregion
}
