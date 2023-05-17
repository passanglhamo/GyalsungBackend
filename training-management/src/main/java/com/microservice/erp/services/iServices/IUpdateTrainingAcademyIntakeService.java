package com.microservice.erp.services.iServices;

import com.microservice.erp.domain.dto.TrainingAcademyCapacityDto;
import org.springframework.http.ResponseEntity;

import java.io.IOException;

public interface IUpdateTrainingAcademyIntakeService {
    ResponseEntity<?> updateTrainingAcademyIntake(TrainingAcademyCapacityDto trainingAcademyCapacityDto);
    ResponseEntity<String> changeAllocateCapacities(String academyAccomodities) throws IOException;

}
