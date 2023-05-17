package com.microservice.erp.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.math.BigInteger;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Validated
public class TrainingAcademyCapacityDto {
    private Integer academyId;
    private String trainingYear;
    private Integer maleCapacityAmount;
    private Integer maleCapacityAmountAllocated;
    private Integer femaleCapacityAmount;
    private Integer femaleCapacityAmountAllocated;
    private String status;
}
