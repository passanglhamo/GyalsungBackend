package com.microservice.erp.services.impl.services;

import com.microservice.erp.domain.dto.TrainingAcademyCapacityDto;
import com.microservice.erp.domain.helper.MessageResponse;
import com.microservice.erp.domain.repository.ITrainingAcademyCapacityRepository;
import com.microservice.erp.services.iServices.ICreateTrainingAcademyCapacityService;
import com.microservice.erp.services.impl.mapper.TrainingAcademyCapacityMapper;
import lombok.RequiredArgsConstructor;
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
    public ResponseEntity<?> save(TrainingAcademyCapacityDto trainingAcademyCapacityDto) throws IOException, ParseException {
        if (!Objects.isNull(repository.findByTrainingYear(trainingAcademyCapacityDto.getTrainingYear()))) {
            return ResponseEntity.ok(new MessageResponse("Year already existed."));
        }

        var trainingAcademyCapacity = repository.save(
                mapper.mapToEntity(trainingAcademyCapacityDto)
        );

        repository.save(trainingAcademyCapacity);

        return ResponseEntity.ok("Training Academy Intake saved successfully.");
    }
}
