package com.microservice.erp.services.iServices;

import com.microservice.erp.domain.dto.ExemptionDto;

import java.util.List;

public interface IReadExemptionService {
    List<ExemptionDto> getAllExemptionList(String authHeader);

    List<ExemptionDto> getExemptionListByStatus(String authHeader, Character status);
}
