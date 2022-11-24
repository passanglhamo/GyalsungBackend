package com.microservice.erp.services.impl.services;

import com.microservice.erp.domain.dto.TrainingAcademyCapacityDto;
import com.microservice.erp.domain.helper.MessageResponse;
import com.microservice.erp.domain.repository.ITrainingAcademyCapacityRepository;
import com.microservice.erp.services.iServices.ICreateTrainingAcademyCapacityService;
import com.microservice.erp.services.impl.mapper.TrainingAcademyCapacityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.ParseException;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CreateTrainingAcademyCapacityService implements ICreateTrainingAcademyCapacityService {
    private final ITrainingAcademyCapacityRepository repository;
    private final TrainingAcademyCapacityMapper mapper;

    @Override
    public ResponseEntity<?> saveTrainingAcaCap(TrainingAcademyCapacityDto trainingAcademyCapacityDto) throws IOException, ParseException {
        if (!Objects.isNull(repository.findByTrainingYearAndAcademyId(trainingAcademyCapacityDto.getTrainingYear(),
                trainingAcademyCapacityDto.getAcademyId()))) {
            return new ResponseEntity<>("Data is already saved for the given year and academy.", HttpStatus.ALREADY_REPORTED);
        }

        var trainingAcademyCapacity = repository.save(
                mapper.mapToEntity(trainingAcademyCapacityDto)
        );

        repository.save(trainingAcademyCapacity);

        return ResponseEntity.ok("Training Academy Intake saved successfully.");
    }
}
