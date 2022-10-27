package com.microservice.erp.services.impl.services;

import com.microservice.erp.domain.dto.HospitalScheduleTimeDto;
import com.microservice.erp.domain.repository.IHospitalScheduleTimeRepository;
import com.microservice.erp.services.iServices.ICreateHospitalScheduleTimeService;
import com.microservice.erp.services.impl.mapper.HospitalScheduleTimeMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class CreateHospitalScheduleTimeService implements ICreateHospitalScheduleTimeService {
    private final IHospitalScheduleTimeRepository repository;
    private final HospitalScheduleTimeMapper mapper;

    public ResponseEntity<?> saveScheduleTimes(HospitalScheduleTimeDto hospitalScheduleTimeDto) throws IOException {

        var hospitalScheduleTime = repository.save(
                mapper.mapToEntity(hospitalScheduleTimeDto)
        );

        repository.save(hospitalScheduleTime);

        return ResponseEntity.ok("Hospital Scheduled Time saved successfully.");
    }
}
