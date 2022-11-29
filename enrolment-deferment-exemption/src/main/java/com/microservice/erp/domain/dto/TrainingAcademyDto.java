package com.microservice.erp.domain.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;

@Setter
@Getter
public class TrainingAcademyDto {
    //region private variables
    private Integer trainingAcaId;
    private String name;
    private String battalion;
    private BigInteger noOfMaleEnrolled;
    private BigInteger noOfFemaleEnrolled;
    //endregion
}
