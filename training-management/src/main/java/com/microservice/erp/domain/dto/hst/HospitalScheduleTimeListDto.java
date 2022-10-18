package com.microservice.erp.domain.dto.hst;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HospitalScheduleTimeListDto {

    private Long id;
    private String startTime;
    private String endTime;

    public static HospitalScheduleTimeListDto withId(
            Long id,
            String startTime,
            String endTime) {
        return new HospitalScheduleTimeListDto(
                id,
                startTime,
                endTime
        );
    }
}
