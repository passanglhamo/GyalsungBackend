package com.microservice.erp.services.iServices.exemption;

import com.microservice.erp.domain.dto.exemption.ExemptionDto;

import java.util.List;

public interface IReadExemptionService {
    List<ExemptionDto> getAllExemptionList(String authHeader);
}
