package com.microservice.erp.services.impl.services;

import com.microservice.erp.domain.entities.DzongkhagHospitalMapping;
import com.microservice.erp.domain.repository.IDzongkhagHospitalMappingRepository;
import com.microservice.erp.services.iServices.IReadDzongkhagHospitalMappingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReadDzongkhagHospitalMappingService implements IReadDzongkhagHospitalMappingService {

    private final IDzongkhagHospitalMappingRepository repository;

    @Override
    public List<DzongkhagHospitalMapping> findAll() {
        return repository.findAll();
    }

    @Override
    public DzongkhagHospitalMapping findById(Long id) {
        return repository.findById(id).get();
    }
}
