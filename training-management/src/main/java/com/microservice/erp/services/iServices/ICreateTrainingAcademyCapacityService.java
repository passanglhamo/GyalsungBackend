package com.microservice.erp.services.iServices;

import com.microservice.erp.domain.dto.TrainingAcademyCapacityDto;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.text.ParseException;

public interface ICreateTrainingAcademyCapacityService {
    ResponseEntity<?> saveTrainingAcaCap(TrainingAcademyCapacityDto trainingAcademyCapacityDto) throws IOException, ParseException;

}
