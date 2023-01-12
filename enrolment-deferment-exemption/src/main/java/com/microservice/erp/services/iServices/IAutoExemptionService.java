package com.microservice.erp.services.iServices;

import com.microservice.erp.domain.dto.AutoExemptionDto;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public interface IAutoExemptionService {
    ResponseEntity<?> readFile(AutoExemptionDto autoExemptionDto);

    ResponseEntity<?> uploadFile(HttpServletRequest request, AutoExemptionDto autoExemptionDto) throws IOException;

}
