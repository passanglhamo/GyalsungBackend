package com.microservice.erp.services.iServices;

import com.microservice.erp.domain.entities.DzongkhagTrainingPreAcaMapping;
import org.springframework.http.ResponseEntity;

public interface IUpdateDzongkhagTrainingMappingService {
    ResponseEntity<?> updateDzongkhagTrainingMapping(DzongkhagTrainingPreAcaMapping dzongkhagTrainingPreAcaMapping);
}
