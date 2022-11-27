package com.microservice.erp.services.iServices;

import com.microservice.erp.domain.dto.DefermentDto;
import com.microservice.erp.domain.entities.DefermentInfo;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletResponse;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

public interface IReadDefermentService {
    List<DefermentDto> getAllDefermentList(String authHeader);

    ResponseEntity<?> downloadFiles(BigInteger defermentId, HttpServletResponse response);

    List<DefermentDto> getDefermentListByToDateStatus(String authHeader,Date toDate, Character status);

    ResponseEntity<?> getDefermentByUserId(BigInteger userId);

    ResponseEntity<?> getDefermentValidation(BigInteger userId);
}
