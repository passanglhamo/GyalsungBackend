package com.microservice.erp.services.iServices;

import com.microservice.erp.domain.entities.TrainingAcademyCapacity;

import java.math.BigInteger;
import java.util.List;

public interface IReadTrainingAcademyCapacityService {
    List<TrainingAcademyCapacity> getAllTrainingAcaCapList();

    TrainingAcademyCapacity getAllTrainingAcaCapById(BigInteger id);
}
