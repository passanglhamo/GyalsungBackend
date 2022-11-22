package com.microservice.erp.services.impl.services;

import com.microservice.erp.domain.dto.EnlistmentScheduleDto;
import com.microservice.erp.services.iServices.IUpdateEnlistmentScheduleService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class UpdateEnlistmentScheduleService implements IUpdateEnlistmentScheduleService {
    @Override
    public ResponseEntity<?> updateEnlistmentSchedule( EnlistmentScheduleDto enlistmentScheduleDto) {
        return null;
    }
}
