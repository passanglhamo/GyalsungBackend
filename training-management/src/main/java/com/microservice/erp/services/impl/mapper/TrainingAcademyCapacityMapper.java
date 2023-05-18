package com.microservice.erp.services.impl.mapper;

import com.microservice.erp.domain.dto.TrainingAcademyCapacityDto;
import com.microservice.erp.domain.entities.TrainingAcademyCapacity;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class TrainingAcademyCapacityMapper {
    public TrainingAcademyCapacity mapToEntity(TrainingAcademyCapacityDto trainingAcademyCapacityDto) {
        return new ModelMapper().map(trainingAcademyCapacityDto, TrainingAcademyCapacity.class);
    }

    public TrainingAcademyCapacityDto mapToDomain(TrainingAcademyCapacity trainingAcademyCapacity) {
        return TrainingAcademyCapacityDto.withId(
                trainingAcademyCapacity.getId(),
                trainingAcademyCapacity.getAcademyId(),
                trainingAcademyCapacity.getTrainingYear(),
                trainingAcademyCapacity.getMaleCapacityAmount(),
                trainingAcademyCapacity.getFemaleCapacityAmount(),
                trainingAcademyCapacity.getStatus()
        );
    }

}
