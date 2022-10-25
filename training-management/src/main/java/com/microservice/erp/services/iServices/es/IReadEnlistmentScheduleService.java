package com.microservice.erp.services.iServices.es;

import com.microservice.erp.domain.entities.EnlistmentSchedule;

import java.util.List;

public interface IReadEnlistmentScheduleService {
    List<EnlistmentSchedule> findAll();

    EnlistmentSchedule findById(Long id);
}
