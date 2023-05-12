package com.microservice.erp.services.iServices;

import com.microservice.erp.domain.entities.DzongkhagTrainingPreAcaMapping;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface IReadDzongkhagTrainingAcaMappingService {

    List<DzongkhagTrainingPreAcaMapping> getAllDzongkhagTrainingList();


    ResponseEntity<?> getAllActiveTrainingsByDzongkhagId(Integer dzongkhagId);
}
