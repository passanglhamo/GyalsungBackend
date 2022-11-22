package com.microservice.erp.services.iServices;

import com.microservice.erp.domain.entities.EnlistmentSchedule;

import java.math.BigInteger;
import java.util.List;

public interface IReadEnlistmentScheduleService {
    List<EnlistmentSchedule> getAllEnlistmentScheduleList();

    EnlistmentSchedule getEnlistmentScheduleById(BigInteger id);
}
