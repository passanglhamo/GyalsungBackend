package com.microservice.erp.services.iServices;

import com.microservice.erp.domain.entities.DzongkhagTrainingMapping;
import org.springframework.http.ResponseEntity;

public interface ICreateDzongkhagTrainingMappingService {
    ResponseEntity<?> saveDzongkhagTraining(DzongkhagTrainingMapping dzongkhagTrainingMapping);
}
