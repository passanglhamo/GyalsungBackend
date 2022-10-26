package com.microservice.erp.services.iServices;

import com.microservice.erp.domain.entities.Reason;

import java.util.List;

public interface IReadReasonService {
    List<Reason> findAll();

    List<Reason> findAllByStatus(Character status);

    Reason findById(Long id);
}
