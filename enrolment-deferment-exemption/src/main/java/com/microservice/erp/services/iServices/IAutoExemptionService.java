package com.microservice.erp.services.iServices;

import com.microservice.erp.domain.dto.AutoExemptionDto;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.math.BigInteger;

public interface IAutoExemptionService {
    ResponseEntity<?> readFile(AutoExemptionDto autoExemptionDto);

    ResponseEntity<?> uploadFile(HttpServletRequest request, AutoExemptionDto autoExemptionDto) throws IOException;

    ResponseEntity<?> getUploadedFiles();

    ResponseEntity<?> deleteFile(BigInteger fileId);
}
