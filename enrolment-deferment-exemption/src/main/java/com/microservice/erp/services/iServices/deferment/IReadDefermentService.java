package com.microservice.erp.services.iServices.deferment;

import com.microservice.erp.domain.dto.deferment.DefermentDto;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletResponse;
import java.math.BigInteger;
import java.util.List;

public interface IReadDefermentService {
    List<DefermentDto> getAllDefermentList(String authHeader);

    ResponseEntity<?> downloadFiles(BigInteger defermentId, HttpServletResponse response);


}
