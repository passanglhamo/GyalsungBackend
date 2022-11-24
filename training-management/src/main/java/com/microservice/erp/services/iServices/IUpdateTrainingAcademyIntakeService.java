package com.microservice.erp.services.iServices;

import com.microservice.erp.domain.dto.TrainingAcademyCapacityDto;
import org.springframework.http.ResponseEntity;

public interface IUpdateTrainingAcademyIntakeService {
    ResponseEntity<?> updateTrainingAcademyIntake(TrainingAcademyCapacityDto trainingAcademyCapacityDto);
}
