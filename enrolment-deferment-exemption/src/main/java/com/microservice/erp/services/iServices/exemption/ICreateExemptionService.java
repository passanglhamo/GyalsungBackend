package com.microservice.erp.services.iServices.exemption;

import com.microservice.erp.domain.dto.exemption.ExemptionDto;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public interface ICreateExemptionService {
    ResponseEntity<?> save(HttpServletRequest request, ExemptionDto exemptionDto) throws IOException;
}
