package com.microservice.erp.services.iServices;

import com.microservice.erp.domain.entities.Reason;

import java.math.BigInteger;
import java.util.List;

public interface IReadReasonService {
    List<Reason> getAllReasonList();

    List<Reason> getAllReasonByStatus(String status);

    Reason getAllReasonById(BigInteger id);
}
