package com.microservice.erp.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.math.BigInteger;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Validated
public class TrainingAcademyCapacityDto {
    private BigInteger id;
    @NotNull
    private Integer academyId;
    @NotNull
    private String trainingYear;
    @NotNull
    @PositiveOrZero(message = "Male capacity can be only positive number")
    private Integer maleCapacityAmount;
    private Integer maleCapacityAmountAllocated;

    @NotNull
    @PositiveOrZero(message = "Female capacity can be only positive number")
    private Integer femaleCapacityAmount;
    private Integer femaleCapacityAmountAllocated;

    @NotNull
    @NotEmpty
    private String status;

    public static TrainingAcademyCapacityDto withId(
            BigInteger id,
            Integer academyId,
            String trainingYear,
            Integer maleCapacityAmount,
            Integer maleCapacityAmountAllocated,
            Integer femaleCapacityAmount,
            Integer femaleCapacityAmountAllocated,
            String status) {
        return new TrainingAcademyCapacityDto(
                id,
                academyId,
                trainingYear,
                maleCapacityAmount,
                maleCapacityAmountAllocated,
                femaleCapacityAmount,
                femaleCapacityAmountAllocated,
                status
        );
    }
}
