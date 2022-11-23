package com.microservice.erp.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigInteger;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HospitalScheduleDateDto {
    private BigInteger id;
    private BigInteger hospitalId;
    private Date appointmentDate;
    private List<HospitalScheduleTimeDto> hospitalScheduleTimeList;
    private Collection<HospitalScheduleTimeDto> hospitalScheduleTimeListDtos;

    public static HospitalScheduleDateDto withId(
            BigInteger id,
            BigInteger hospitalId,
            Date appointmentDate,
            List<HospitalScheduleTimeDto> hospitalScheduleTimeList,
            Collection<HospitalScheduleTimeDto> hospitalScheduleTimeListDtos) {
        return new HospitalScheduleDateDto(
                id,
                hospitalId,
                appointmentDate,
                hospitalScheduleTimeList,
                hospitalScheduleTimeListDtos
        );
    }

}
