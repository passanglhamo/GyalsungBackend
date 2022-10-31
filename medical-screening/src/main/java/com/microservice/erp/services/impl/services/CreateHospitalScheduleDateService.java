package com.microservice.erp.services.impl.services;

import com.microservice.erp.domain.dto.HospitalScheduleDateDto;
import com.microservice.erp.domain.repositories.IHospitalScheduleDateRepository;
import com.microservice.erp.services.iServices.ICreateHospitalScheduleDateService;
import com.microservice.erp.services.impl.mapper.HospitalScheduleTimeMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class CreateHospitalScheduleDateService implements ICreateHospitalScheduleDateService {
    private final IHospitalScheduleDateRepository repository;
    private final HospitalScheduleTimeMapper mapper;

    public ResponseEntity<?> saveScheduleDate(HospitalScheduleDateDto hospitalScheduleTimeDto) throws IOException {

        var hospitalScheduleDate = repository.save(
                mapper.mapToEntity(hospitalScheduleTimeDto)
        );

        repository.save(hospitalScheduleDate);

        return ResponseEntity.ok("Hospital Scheduled Date saved successfully.");
    }
}
