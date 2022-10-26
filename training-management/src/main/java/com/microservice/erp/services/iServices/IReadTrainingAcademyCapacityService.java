package com.microservice.erp.services.iServices;

import com.microservice.erp.domain.entities.TrainingAcademyCapacity;

import java.util.List;

public interface IReadTrainingAcademyCapacityService {
    List<TrainingAcademyCapacity> findAll();

    TrainingAcademyCapacity findById(Long id);
}
