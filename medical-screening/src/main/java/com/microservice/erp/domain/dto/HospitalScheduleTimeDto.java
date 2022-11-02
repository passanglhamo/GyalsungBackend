package com.microservice.erp.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HospitalScheduleTimeDto {

    private Long id;
    private String startTime;
    private String endTime;

    public static HospitalScheduleTimeDto withId(
            Long id,
            String startTime,
            String endTime) {
        return new HospitalScheduleTimeDto(
                id,
                startTime,
                endTime
        );
    }
}
