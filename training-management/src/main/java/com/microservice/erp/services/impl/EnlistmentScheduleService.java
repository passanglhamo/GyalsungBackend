package com.microservice.erp.services.impl;

import com.microservice.erp.domain.entities.EnlistmentSchedule;
import com.microservice.erp.domain.repository.IEnlistmentScheduleRepository;
import com.microservice.erp.services.iServices.IEnlistmentScheduleService;
import org.springframework.stereotype.Service;

/**
 * @author Rajib Kumer Ghosh
 *
 */

@Service
public class EnlistmentScheduleService implements IEnlistmentScheduleService {

    private IEnlistmentScheduleRepository repository;

    public EnlistmentScheduleService(IEnlistmentScheduleRepository repository) {
        this.repository = repository;
    }

    @Override
    public void setIEnlistmentScheduleRepository(IEnlistmentScheduleRepository repository) {
        this.repository = repository;
    }

    @Override
    public String getHello() {
        String msg = "Hello Micro Service Developer ....!";
        return msg;
    }

}
