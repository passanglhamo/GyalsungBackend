package com.microservice.erp.services.iServices;

import com.microservice.erp.domain.entities.Reason;
import org.springframework.http.ResponseEntity;

public interface IUpdateReasonService {
    ResponseEntity<?> updateReason(Reason reason);
}
