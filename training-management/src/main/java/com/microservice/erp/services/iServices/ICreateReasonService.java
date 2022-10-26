package com.microservice.erp.services.iServices;

import com.microservice.erp.domain.entities.Reason;

public interface ICreateReasonService {
    Reason add(Reason reason);
}
