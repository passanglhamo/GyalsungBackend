package com.microservice.erp.services.impl.services.tac;

import com.microservice.erp.domain.entities.TrainingAcademyCapacity;
import com.microservice.erp.domain.repository.ITrainingAcademyCapacityRepository;
import com.microservice.erp.services.iServices.tac.IReadTrainingAcademyCapacityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TrainingAcademyCapacityService implements IReadTrainingAcademyCapacityService {
    private final ITrainingAcademyCapacityRepository repository;

    public List<TrainingAcademyCapacity> findAll() {
        return repository.findAll();
    }
}
