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

    List<DefermentDto> getDefermentListByDefermentYearReasonStatus(String authHeader,String defermentYear, BigInteger reasonId, Character status);

    ResponseEntity<?> getDefermentByUserId(BigInteger userId);

    ResponseEntity<?> getDefermentValidation(BigInteger userId);
}
