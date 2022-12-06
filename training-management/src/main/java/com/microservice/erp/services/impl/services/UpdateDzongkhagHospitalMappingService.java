package com.microservice.erp.services.impl.services;

import com.microservice.erp.domain.entities.DzongkhagHospitalMapping;
import com.microservice.erp.domain.repository.IDzongkhagHospitalMappingRepository;
import com.microservice.erp.services.iServices.IUpdateDzongkhagHospitalMappingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdateDzongkhagHospitalMappingService implements IUpdateDzongkhagHospitalMappingService {

    private final IDzongkhagHospitalMappingRepository repository;

    @Override
    public ResponseEntity<?> updateDzongkhagHospitalMapping(DzongkhagHospitalMapping dzongkhagHospitalMapping) {

        boolean defermentInfoExist = repository.existsByHospitalIdAndIdNot(dzongkhagHospitalMapping.getHospitalId(),
                dzongkhagHospitalMapping.getId());

        if (defermentInfoExist) {
            return new ResponseEntity<>("Selected hospital is already mapped.", HttpStatus.ALREADY_REPORTED);
        }

        repository.findById(dzongkhagHospitalMapping.getId()).map(d -> {
            d.setDzongkhagId(dzongkhagHospitalMapping.getDzongkhagId());
            d.setHospitalId(dzongkhagHospitalMapping.getHospitalId());
            d.setStatus(dzongkhagHospitalMapping.getStatus());
            repository.save(d);
            return d;
        });

        return ResponseEntity.ok("Dzongkhag and hospital mapping information updated successfully.");
    }
}
