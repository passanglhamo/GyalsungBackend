package com.microservice.erp.services.impl.mapper;

import com.microservice.erp.domain.dto.HospitalScheduleTimeDto;
import com.microservice.erp.domain.dto.HospitalScheduleTimeListDto;
import com.microservice.erp.domain.entities.HospitalScheduleTime;
import com.microservice.erp.domain.entities.HospitalScheduleTimeList;
import com.microservice.erp.domain.helper.AppointmentStatus;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class HospitalScheduleTimeMapper {

    public HospitalScheduleTime mapToEntity(HospitalScheduleTimeDto hospitalScheduleTimeDto) {
        HospitalScheduleTime hospitalScheduleTime = new ModelMapper().map(hospitalScheduleTimeDto, HospitalScheduleTime.class);
        hospitalScheduleTime.setStatus(AppointmentStatus.Available.value());
        hospitalScheduleTime.setHospitalScheduleTimeLists(
                hospitalScheduleTimeDto.getHospitalScheduleTimeList()
                        .stream()
                        .map(ta ->
                                new HospitalScheduleTimeList(
                                        ta.getStartTime(),
                                        ta.getEndTime(),
                                        hospitalScheduleTime
                                )
                        )
                        .collect(Collectors.toSet())
        );

        return hospitalScheduleTime;
    }
    public HospitalScheduleTimeDto mapToDomain(HospitalScheduleTime hospitalScheduleTime) {
        return HospitalScheduleTimeDto.withId(
                hospitalScheduleTime.getId(),
                hospitalScheduleTime.getHospitalId(),
                hospitalScheduleTime.getAppointmentDate(),
                null,
                hospitalScheduleTime.getHospitalScheduleTimeLists()
                        .stream()
                        .map(ta ->
                                HospitalScheduleTimeListDto.withId(
                                        ta.getId(),
                                        ta.getStartTime(),
                                        ta.getEndTime()
                                )
                        )
                        .collect(Collectors.toUnmodifiableSet())
        );
    }

}
