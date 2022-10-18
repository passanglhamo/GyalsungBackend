package com.microservice.erp.services.iServices.re;

import com.microservice.erp.domain.entities.Reason;

import java.util.List;

public interface IReadReasonService {
    List<Reason> findAll();

    List<Reason> findAllByStatus(Character status);
}
