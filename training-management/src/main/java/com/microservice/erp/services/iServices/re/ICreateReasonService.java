package com.microservice.erp.services.iServices.re;

import com.microservice.erp.domain.entities.Reason;

public interface ICreateReasonService {
    Reason add(Reason reason);
}
