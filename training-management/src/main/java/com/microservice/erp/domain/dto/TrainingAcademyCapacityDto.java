package com.microservice.erp.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TrainingAcademyCapacityDto {
    private Long id;
    @NotNull
    private Integer academyId;
    @NotNull
    private String trainingYear;
    @NotNull
    private Integer maleCapacityAmount;
    @NotNull
    private Integer femaleCapacityAmount;
    @NotNull
    @NotEmpty
    private String status;

    public static TrainingAcademyCapacityDto withId(
            Long id,
            Integer academyId,
            String trainingYear,
            Integer maleCapacityAmount,
            Integer femaleCapacityAmount,
            String status) {
        return new TrainingAcademyCapacityDto(
                id,
                academyId,
                trainingYear,
                maleCapacityAmount,
                femaleCapacityAmount,
                status
        );
    }
}
