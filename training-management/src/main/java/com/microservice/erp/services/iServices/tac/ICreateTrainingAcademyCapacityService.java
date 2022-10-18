package com.microservice.erp.services.iServices.tac;

import com.microservice.erp.domain.dto.es.TrainingAcademyCapacityDto;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.text.ParseException;

public interface ICreateTrainingAcademyCapacityService {
    ResponseEntity<?> save(TrainingAcademyCapacityDto trainingAcademyCapacityDto) throws IOException, ParseException;

}
