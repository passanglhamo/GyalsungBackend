package com.microservice.erp.services.impl.services;

import com.microservice.erp.domain.dto.HospitalScheduleDateDto;
import com.microservice.erp.domain.dto.HospitalScheduleTimeDto;
import com.microservice.erp.domain.entities.HospitalScheduleDate;
import com.microservice.erp.domain.entities.HospitalScheduleTime;
import com.microservice.erp.domain.helper.DateConversion;
import com.microservice.erp.domain.repositories.IHospitalScheduleDateRepository;
import com.microservice.erp.domain.repositories.IHospitalScheduleTimeRepository;
import com.microservice.erp.services.iServices.ICreateHospitalScheduleDateService;
import com.microservice.erp.services.impl.mapper.HospitalScheduleTimeMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CreateHospitalScheduleDateService implements ICreateHospitalScheduleDateService {
    private final IHospitalScheduleDateRepository repository;
    private final IHospitalScheduleTimeRepository hospitalScheduleTimeRepository;
    private final HospitalScheduleTimeMapper mapper;

    public ResponseEntity<?> saveScheduleDate(HospitalScheduleDateDto hospitalScheduleDateDto) {

//        if (!Objects.isNull(hospitalScheduleDateDto.getHospitalScheduleTimeList())) {
//            HospitalScheduleTimeDto hospitalScheduleTimeDtoVal = hospitalScheduleDateDto.getHospitalScheduleTimeList()
//                    .stream()
//                    .filter(hospitalScheduleTimeDto -> !Objects.isNull(repository.getHospitalScheduleByAppDateAndTime(hospitalScheduleDateDto.getAppointmentDate(),
//                            DateConversion.convertToDate(hospitalScheduleTimeDto.getStartTime()))))
//                    .findFirst().orElse(null);
//            if (!Objects.isNull(hospitalScheduleTimeDtoVal)) {
//                return new ResponseEntity<>("Hospital time already exist.", HttpStatus.ALREADY_REPORTED);
//            }
//        }


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
