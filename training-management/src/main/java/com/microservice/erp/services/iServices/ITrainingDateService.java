package com.microservice.erp.services.iServices;

import com.microservice.erp.domain.entities.TrainingDate;
import org.springframework.http.ResponseEntity;

import java.math.BigInteger;

public interface ITrainingDateService {
    ResponseEntity<?> saveTrainingDate(BigInteger userId,TrainingDate trainingDate);

    ResponseEntity<?> getTrainingDate();

    ResponseEntity<?> getActiveTrainingDate();

    ResponseEntity<?> updateTrainingDate(BigInteger userId, TrainingDate trainingDate);
}
