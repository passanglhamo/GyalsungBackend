package com.microservice.erp.services.impl.services;

import com.microservice.erp.domain.dto.TrainingAcademyCapacityDto;
import com.microservice.erp.domain.repository.ITrainingAcademyCapacityRepository;
import com.microservice.erp.services.iServices.IUpdateTrainingAcademyIntakeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UpdateTrainingAcademyIntakeService implements IUpdateTrainingAcademyIntakeService {

    private final ITrainingAcademyCapacityRepository repository;

    @Override
    public ResponseEntity<?> updateTrainingAcademyIntake(TrainingAcademyCapacityDto trainingAcademyCapacityDto) {
        if (!Objects.isNull(repository.findByTrainingYearAndAcademyIdAndIdNot(trainingAcademyCapacityDto.getTrainingYear(),
                trainingAcademyCapacityDto.getAcademyId(), trainingAcademyCapacityDto.getId()))) {
            return new ResponseEntity<>("Data is already saved for the given year and academy.", HttpStatus.ALREADY_REPORTED);
        }
        repository.findById(trainingAcademyCapacityDto.getId()).stream().map(d -> {
            d.setAcademyId(trainingAcademyCapacityDto.getAcademyId());
            d.setTrainingYear(trainingAcademyCapacityDto.getTrainingYear());
            d.setMaleCapacityAmount(trainingAcademyCapacityDto.getMaleCapacityAmount());
            d.setFemaleCapacityAmount(trainingAcademyCapacityDto.getFemaleCapacityAmount());
            d.setStatus(trainingAcademyCapacityDto.getStatus());
            repository.save(d);
            return d;
        });

        return ResponseEntity.ok("Training Academy Intake updated successfully.");
    }
}
