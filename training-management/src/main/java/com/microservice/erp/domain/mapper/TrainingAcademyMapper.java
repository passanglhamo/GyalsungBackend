package com.microservice.erp.domain.mapper;


import com.microservice.erp.domain.dto.TrainingAcademyCapacityDto;
import com.microservice.erp.domain.entities.TrainingAcademyCapacity;
import org.springframework.stereotype.Component;

@Component
public class TrainingAcademyMapper {


    public TrainingAcademyCapacityDto mapToDomain(TrainingAcademyCapacity trainingAcademyCapacity) {
        return TrainingAcademyCapacityDto.withId(
                trainingAcademyCapacity.getId(),
                trainingAcademyCapacity.getAcademyId(),
                trainingAcademyCapacity.getTrainingYear(),
                trainingAcademyCapacity.getMaleCapacityAmount(),
                trainingAcademyCapacity.getFemaleCapacityAmountAllocated(),
                trainingAcademyCapacity.getFemaleCapacityAmount(),
                trainingAcademyCapacity.getFemaleCapacityAmountAllocated(),
                trainingAcademyCapacity.getStatus()
        );
    }
}
