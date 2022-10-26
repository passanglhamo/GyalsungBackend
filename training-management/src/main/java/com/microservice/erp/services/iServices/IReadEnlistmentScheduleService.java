package com.microservice.erp.services.iServices;

import com.microservice.erp.domain.entities.EnlistmentSchedule;

import java.util.List;

public interface IReadEnlistmentScheduleService {
    List<EnlistmentSchedule> findAll();

    EnlistmentSchedule findById(Long id);
}
