package com.microservice.erp.services.impl.services;

import com.microservice.erp.domain.entities.TrainingAcademyCapacity;
import com.microservice.erp.domain.repository.ITrainingAcademyCapacityRepository;
import com.microservice.erp.services.iServices.IReadTrainingAcademyCapacityService;
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

    @Override
    public TrainingAcademyCapacity findById(Long id) {
        return repository.findById(id).get();
    }
}
