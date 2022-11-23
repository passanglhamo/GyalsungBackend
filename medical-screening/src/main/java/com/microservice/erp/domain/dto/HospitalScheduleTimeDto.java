package com.microservice.erp.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigInteger;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HospitalScheduleTimeDto {

    private BigInteger id;
    private String startTime;
    private String endTime;
    private Date startDateTime;
    private Date endDateTime;
    private Character bookedStatus;


    public static HospitalScheduleTimeDto withId(
            BigInteger id,
            String startTime,
            String endTime,
            Date startDateTime,
            Date endDateTime
            , Character bookedStatus) {
        return new HospitalScheduleTimeDto(
                id,
                startTime,
                endTime,
                startDateTime,
                endDateTime,
                bookedStatus

        );
    }
}
