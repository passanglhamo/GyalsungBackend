package com.microservice.erp.services.iServices;

import com.microservice.erp.domain.dto.EnlistmentScheduleDto;
import org.springframework.http.ResponseEntity;

public interface IUpdateEnlistmentScheduleService {
    ResponseEntity<?> updateEnlistmentSchedule(EnlistmentScheduleDto enlistmentScheduleDto);
}
