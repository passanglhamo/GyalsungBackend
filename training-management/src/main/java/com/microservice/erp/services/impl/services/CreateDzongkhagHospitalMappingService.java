package com.microservice.erp.services.impl.services;

import com.microservice.erp.domain.entities.DzongkhagHospitalMapping;
import com.microservice.erp.domain.helper.MessageResponse;
import com.microservice.erp.domain.repository.IDzongkhagHospitalMappingRepository;
import com.microservice.erp.services.iServices.ICreateDzongkhagHospitalMappingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateDzongkhagHospitalMappingService implements ICreateDzongkhagHospitalMappingService {

    private final IDzongkhagHospitalMappingRepository repository;

    @Override
    public ResponseEntity<?> saveDzongkhagHospital(DzongkhagHospitalMapping dzongkhagHospitalMapping) {

        boolean defermentInfoExist = repository.existsByHospitalId(dzongkhagHospitalMapping.getHospitalId());

        if (defermentInfoExist) {
            return new ResponseEntity<>("Selected hospital is already mapped.", HttpStatus.ALREADY_REPORTED);
        }

        repository.save(dzongkhagHospitalMapping);
        return ResponseEntity.ok("Dzongkhag and hospital mapped successfully.");
    }
}
