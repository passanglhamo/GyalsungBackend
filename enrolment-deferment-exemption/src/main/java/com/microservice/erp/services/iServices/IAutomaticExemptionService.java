package com.microservice.erp.services.iServices;

import com.microservice.erp.domain.dto.AutomaticExemptionDto;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public interface IAutomaticExemptionService {
    ResponseEntity<?> readFile(AutomaticExemptionDto automaticExemptionDto);

    ResponseEntity<?> uploadFile(HttpServletRequest request, AutomaticExemptionDto automaticExemptionDto) throws IOException;

}
