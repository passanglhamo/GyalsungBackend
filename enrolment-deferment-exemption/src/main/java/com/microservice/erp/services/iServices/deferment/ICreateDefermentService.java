package com.microservice.erp.services.iServices.deferment;

import com.microservice.erp.domain.dto.deferment.DefermentDto;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @author Passang Lhamo
 */
public interface ICreateDefermentService {

    ResponseEntity<?> save(HttpServletRequest request, DefermentDto defermentDto) throws IOException;

}
