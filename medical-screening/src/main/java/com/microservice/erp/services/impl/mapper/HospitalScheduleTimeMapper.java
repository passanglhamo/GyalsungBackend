package com.microservice.erp.services.impl.mapper;

import com.microservice.erp.domain.dto.HospitalScheduleDateDto;
import com.microservice.erp.domain.dto.HospitalScheduleTimeDto;
import com.microservice.erp.domain.entities.HospitalScheduleDate;
import com.microservice.erp.domain.entities.HospitalScheduleTime;
import com.microservice.erp.domain.helper.AppointmentStatus;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class HospitalScheduleTimeMapper {

    public HospitalScheduleDate mapToEntity(HospitalScheduleDateDto hospitalScheduleTimeDto) {
        HospitalScheduleDate hospitalScheduleTime = new ModelMapper().map(hospitalScheduleTimeDto, HospitalScheduleDate.class);
        hospitalScheduleTime.setStatus(AppointmentStatus.Available.value());
        hospitalScheduleTime.setHospitalScheduleTimeLists(
                hospitalScheduleTimeDto.getHospitalScheduleTimeList()
                        .stream()
                        .map(ta ->
                                new HospitalScheduleTime(
                                        ta.getStartTime(),
                                        ta.getEndTime(),
                                        hospitalScheduleTime
                                )
                        )
                        .collect(Collectors.toSet())
        );

        return hospitalScheduleTime;
    }
    public HospitalScheduleDateDto mapToDomain(HospitalScheduleDate hospitalScheduleTime) {
        return HospitalScheduleDateDto.withId(
                hospitalScheduleTime.getId(),
                hospitalScheduleTime.getHospitalId(),
                hospitalScheduleTime.getAppointmentDate(),
                null,
                hospitalScheduleTime.getHospitalScheduleTimeLists()
                        .stream()
                        .map(ta ->
                                HospitalScheduleTimeDto.withId(
                                        ta.getId(),
                                        ta.getStartTime(),
                                        ta.getEndTime()
                                )
                        )
                        .collect(Collectors.toUnmodifiableSet())
        );
    }

}
