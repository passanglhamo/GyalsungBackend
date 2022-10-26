package com.microservice.erp.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Collection;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HospitalScheduleTimeDto {
    private Long id;
    private Long hospitalId;
    private Date appointmentDate;
    private List<HospitalScheduleTimeListDto> hospitalScheduleTimeList;
    private Collection<HospitalScheduleTimeListDto> hospitalScheduleTimeListDtos;

    public static HospitalScheduleTimeDto withId(
            Long id,
            Long hospitalId,
            Date appointmentDate,
            List<HospitalScheduleTimeListDto> hospitalScheduleTimeList,
            Collection<HospitalScheduleTimeListDto> hospitalScheduleTimeListDtos) {
        return new HospitalScheduleTimeDto(
                id,
                hospitalId,
                appointmentDate,
                hospitalScheduleTimeList,
                hospitalScheduleTimeListDtos
        );
    }

}
