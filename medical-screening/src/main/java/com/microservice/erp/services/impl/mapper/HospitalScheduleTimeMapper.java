package com.microservice.erp.services.impl.mapper;

import com.microservice.erp.domain.dto.HospitalScheduleDateDto;
import com.microservice.erp.domain.dto.HospitalScheduleTimeDto;
import com.microservice.erp.domain.entities.HospitalScheduleDate;
import com.microservice.erp.domain.entities.HospitalScheduleTime;
import com.microservice.erp.domain.helper.AppointmentStatus;
import com.microservice.erp.domain.helper.DateConversion;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Date;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class HospitalScheduleTimeMapper {
    public HospitalScheduleDate mapToEntity(HospitalScheduleDateDto hospitalScheduleTimeDto) {
        HospitalScheduleDate hospitalScheduleTime = new ModelMapper().map(hospitalScheduleTimeDto, HospitalScheduleDate.class);
        hospitalScheduleTime.setStatus(AppointmentStatus.Available.value());
        hospitalScheduleTime.setHospitalScheduleTimeLists(
                hospitalScheduleTimeDto.getHospitalScheduleTimeList()
                        .stream()
                        .map(ta ->
                                new HospitalScheduleTime(
                                        DateConversion.convertToDate(ta.getStartTime()),
                                        DateConversion.convertToDate(ta.getEndTime()),
                                        'A',
                                        null,
                                        null,
                                        null,
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
                                        ta.getId()
                                        , null
                                        , null
                                        , ta.getStartTime()
                                        , ta.getEndTime()
                                        , ta.getBookStatus()
                                )
                        )
                        .collect(Collectors.toUnmodifiableSet())
        );
    }


}
