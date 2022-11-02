package com.microservice.erp.services.impl.services;

import com.microservice.erp.domain.dto.HospitalScheduleTimeDto;
import com.microservice.erp.domain.repositories.IHospitalScheduleTimeRepository;
import com.microservice.erp.services.iServices.IUpdateHospitalScheduleTimeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class UpdateHospitalScheduleTimeService implements IUpdateHospitalScheduleTimeService {
    private final IHospitalScheduleTimeRepository repository;
    public ResponseEntity<?> updateScheduleTimes(HospitalScheduleTimeDto hospitalScheduleTimeListDto) throws IOException {

        repository.findById(hospitalScheduleTimeListDto.getId()).map(d -> {
            d.setStartTime(hospitalScheduleTimeListDto.getStartTime());
            d.setEndTime(hospitalScheduleTimeListDto.getEndTime());
            return repository.save(d);
        });


        return ResponseEntity.ok("Hospital Scheduled Time updated successfully.");
    }
}
