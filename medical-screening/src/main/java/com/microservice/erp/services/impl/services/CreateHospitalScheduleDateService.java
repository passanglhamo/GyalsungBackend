package com.microservice.erp.services.impl.services;

import com.microservice.erp.domain.dto.HospitalScheduleDateDto;
import com.microservice.erp.domain.entities.HospitalScheduleDate;
import com.microservice.erp.domain.entities.HospitalScheduleTime;
import com.microservice.erp.domain.repositories.IHospitalScheduleDateRepository;
import com.microservice.erp.domain.repositories.IHospitalScheduleTimeRepository;
import com.microservice.erp.services.iServices.ICreateHospitalScheduleDateService;
import com.microservice.erp.services.impl.mapper.HospitalScheduleTimeMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateHospitalScheduleDateService implements ICreateHospitalScheduleDateService {
    private final IHospitalScheduleDateRepository repository;
    private final IHospitalScheduleTimeRepository hospitalScheduleTimeRepository;
    private final HospitalScheduleTimeMapper mapper;

    public ResponseEntity<?> saveScheduleDate(HospitalScheduleDateDto hospitalScheduleDateDto) {

        if (!repository.existsByAppointmentDateAndHospitalId(hospitalScheduleDateDto.getAppointmentDate(),
                hospitalScheduleDateDto.getHospitalId())) {
            repository.save(
                    mapper.mapToEntity(hospitalScheduleDateDto)
            );
        } else {
            hospitalScheduleDateDto.getHospitalScheduleTimeList().forEach(hospitalScheduleTimeDto -> {
                HospitalScheduleDate hospitalScheduleDate = repository.findAllByAppointmentDateAndHospitalId(
                        hospitalScheduleDateDto.getAppointmentDate(), hospitalScheduleDateDto.getHospitalId()).get(0);
                HospitalScheduleTime hospitalScheduleTime = mapper.mapToHospitalTimeEntity(hospitalScheduleTimeDto, hospitalScheduleDate);
                hospitalScheduleTimeRepository.save(hospitalScheduleTime);
            });

        }


        return ResponseEntity.ok("Hospital Scheduled Date saved successfully.");
    }


}
