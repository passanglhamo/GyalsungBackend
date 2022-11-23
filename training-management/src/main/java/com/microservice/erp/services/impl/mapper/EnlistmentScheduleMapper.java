package com.microservice.erp.services.impl.mapper;

import com.microservice.erp.domain.dto.EnlistmentScheduleDto;
import com.microservice.erp.domain.entities.EnlistmentSchedule;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class EnlistmentScheduleMapper {
    public EnlistmentSchedule mapToEntity(EnlistmentScheduleDto enlistmentScheduleDto) {
        EnlistmentSchedule enlistmentSchedule = new ModelMapper().map(enlistmentScheduleDto, EnlistmentSchedule.class);
        enlistmentSchedule.setFromDate(enlistmentScheduleDto.getFromDate());
        enlistmentSchedule.setToDate(enlistmentScheduleDto.getToDate());
        enlistmentSchedule.setStatus(enlistmentScheduleDto.getStatus());
        return enlistmentSchedule;
    }

    public EnlistmentSchedule mapToUpdateEntity(EnlistmentScheduleDto enlistmentScheduleDto) {
        EnlistmentSchedule enlistmentSchedule = new EnlistmentSchedule();
        enlistmentSchedule.setId(enlistmentScheduleDto.getId());
        enlistmentSchedule.setFromDate(enlistmentScheduleDto.getFromDate());
        enlistmentSchedule.setToDate(enlistmentScheduleDto.getToDate());
        enlistmentSchedule.setStatus(enlistmentScheduleDto.getStatus());
        return enlistmentSchedule;
    }


}
