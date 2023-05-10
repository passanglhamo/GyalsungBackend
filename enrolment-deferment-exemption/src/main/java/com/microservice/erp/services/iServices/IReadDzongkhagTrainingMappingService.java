package com.microservice.erp.services.iServices;

import com.microservice.erp.domain.entities.DzongkhagTrainingMapping;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface IReadDzongkhagTrainingMappingService {

    List<DzongkhagTrainingMapping> getAllDzongkhagTrainingList();

    List<DzongkhagTrainingMapping> getAllDzongkhagTrainingByStatus(String status);

    ResponseEntity<?> getAllActiveTrainingsByDzongkhagId(Integer dzongkhagId);
}
