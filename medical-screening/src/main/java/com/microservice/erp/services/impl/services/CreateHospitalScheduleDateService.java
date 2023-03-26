package com.microservice.erp.services.impl.services;

import com.microservice.erp.domain.dto.HospitalScheduleDateDto;
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

import java.util.List;
import java.util.stream.Collectors;

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
            List<ResponseEntity<?>> responses = hospitalScheduleDateDto.getHospitalScheduleTimeList().stream()
                    .map(hospitalScheduleTimeDto -> {
                        List<HospitalScheduleDate> scheduleDates = repository.findAllByAppointmentDateAndHospitalId(
                                hospitalScheduleDateDto.getAppointmentDate(), hospitalScheduleDateDto.getHospitalId());
                        if (scheduleDates.isEmpty()) {
                            return new ResponseEntity<>("Hospital schedule date not found", HttpStatus.NOT_FOUND);
                        }
                        HospitalScheduleDate hospitalScheduleDate = scheduleDates.get(0);
                        HospitalScheduleTime conflictingTime = hospitalScheduleDate.getHospitalScheduleTimeLists().stream()
                                .filter(time -> DateConversion.extractTimeFromDate(DateConversion.convertToDate(hospitalScheduleTimeDto.getStartTime()))
                                        .compareTo(DateConversion.extractTimeFromDate(time.getStartTime())) >= 0
                                        && DateConversion.extractTimeFromDate(DateConversion.convertToDate(hospitalScheduleTimeDto.getStartTime()))
                                        .compareTo(DateConversion.extractTimeFromDate(time.getEndTime())) <= 0)
                                .findFirst()
                                .orElse(null);
                        if (conflictingTime != null) {
                            return new ResponseEntity<>("Schedule for date: " + DateConversion.changeDateFormat(hospitalScheduleDateDto.getAppointmentDate())
                                    + " and time: " + DateConversion.getTimeAMPMFormat(DateConversion.extractTimeFromDate(DateConversion.convertToDate(hospitalScheduleTimeDto.getStartTime()))) + " already exists.", HttpStatus.ALREADY_REPORTED);
                        } else {
                            return ResponseEntity.ok("Validation is successful.");
                        }
                    })
                    .collect(Collectors.toList());
            ResponseEntity<?> responseEntity = responses.stream()
                    .filter(response -> response.getStatusCode() != HttpStatus.OK)
                    .findFirst()
                    .orElse(ResponseEntity.ok("Hospital Scheduled Date saved successfully."));

            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                hospitalScheduleDateDto.getHospitalScheduleTimeList().forEach(hospitalScheduleTimeDto -> {
                    HospitalScheduleDate hospitalScheduleDate = repository.findAllByAppointmentDateAndHospitalId(
                            hospitalScheduleDateDto.getAppointmentDate(), hospitalScheduleDateDto.getHospitalId()).get(0);
                    HospitalScheduleTime hospitalScheduleTime = mapper.mapToHospitalTimeEntity(hospitalScheduleTimeDto, hospitalScheduleDate);
                    hospitalScheduleTimeRepository.save(hospitalScheduleTime);
                });
            } else {
                return responseEntity;
            }


        }


        return ResponseEntity.ok("Hospital Scheduled Date saved successfully.");
    }


}
