package com.microservice.erp.services.impl.services;

import com.microservice.erp.domain.entities.DzongkhagHospitalMapping;
import com.microservice.erp.domain.repository.IDzongkhagHospitalMappingRepository;
import com.microservice.erp.services.iServices.ICreateDzongkhagHospitalMappingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateDzongkhagHospitalMappingService implements ICreateDzongkhagHospitalMappingService {

    private final IDzongkhagHospitalMappingRepository repository;

    @Override
    public DzongkhagHospitalMapping saveDzongkhagHospital(DzongkhagHospitalMapping dzongkhagHospitalMapping) {
        return repository.save(dzongkhagHospitalMapping);
    }
}
