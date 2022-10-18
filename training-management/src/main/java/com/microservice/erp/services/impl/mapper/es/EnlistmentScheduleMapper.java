package com.microservice.erp.services.impl.mapper.es;

import com.microservice.erp.domain.dto.es.EnlistmentScheduleDto;
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


}
