package com.microservice.erp.services.impl.services;

import com.microservice.erp.domain.dto.TrainingAcademyCapacityDto;
import com.microservice.erp.domain.entities.TrainingAcademyCapacity;
import com.microservice.erp.domain.mapper.TrainingAcademyMapper;
import com.microservice.erp.domain.repository.ITrainingAcademyCapacityRepository;
import com.microservice.erp.services.iServices.IReadTrainingAcademyCapacityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReadTrainingAcademyCapacityService implements IReadTrainingAcademyCapacityService {
    private final ITrainingAcademyCapacityRepository repository;
    private final TrainingAcademyMapper mapper;

    public List<TrainingAcademyCapacityDto> getAllTrainingAcaCapList() {
        return repository.findAll()
                .stream()
                .map(mapper::mapToDomain)
                .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public TrainingAcademyCapacity getAllTrainingAcaCapById(BigInteger id) {
        return repository.findById(id).get();
    }

    @Override
    public TrainingAcademyCapacity getAllTrainingAcaCapByAcademyId(String year, Integer academyId) {
        return repository.findByTrainingYearAndAcademyId(year, academyId).get();
    }


}
