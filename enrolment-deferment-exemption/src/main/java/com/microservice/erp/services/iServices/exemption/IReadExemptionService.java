package com.microservice.erp.services.iServices.exemption;

import com.microservice.erp.domain.dto.exemption.ExemptionDto;

import java.util.Collection;

public interface IReadExemptionService {
    Collection<ExemptionDto> getAll();
}
