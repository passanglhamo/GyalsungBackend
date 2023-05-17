package com.microservice.erp.services.iServices;

import com.microservice.erp.domain.dto.TrainingAcademyCapacityDto;
import com.microservice.erp.domain.entities.TrainingAcademyCapacity;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.math.BigInteger;
import java.util.List;

public interface IReadTrainingAcademyCapacityService {
    List<TrainingAcademyCapacityDto> getAllTrainingAcaCapList();

    TrainingAcademyCapacity getAllTrainingAcaCapById(BigInteger id);

    TrainingAcademyCapacity getAllTrainingAcaCapByAcademyId(String year,Integer academyId);

}
