package com.microservice.erp.services.impl.services;

import com.microservice.erp.domain.dto.HospitalScheduleTimeDto;
import com.microservice.erp.domain.entities.Dzongkhag;
import com.microservice.erp.domain.entities.Hospital;
import com.microservice.erp.domain.entities.HospitalScheduleTime;
import com.microservice.erp.domain.helper.MessageResponse;
import com.microservice.erp.domain.repository.IDzongkhagRepository;
import com.microservice.erp.domain.repository.IHospitalRepository;
import com.microservice.erp.domain.repository.IHospitalScheduleTimeRepository;
import com.microservice.erp.services.iServices.IReadHospitalScheduleTimeService;
import com.microservice.erp.services.impl.mapper.HospitalScheduleTimeMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReadHospitalScheduleTimeService implements IReadHospitalScheduleTimeService {

    private final IHospitalScheduleTimeRepository repository;
    private final IHospitalRepository iHospitalRepository;
    private final HospitalScheduleTimeMapper mapper;
    private final IHospitalScheduleTimeRepository iHospitalScheduleTimeRepository;

    public Collection<HospitalScheduleTimeDto> getAllScheduleTimesById(Long dzoHosId) {
        return repository.findAllByHospitalId(dzoHosId)
                .stream()
                .map(mapper::mapToDomain)
                .collect(Collectors.toUnmodifiableList());
    }



    @Override
    public ResponseEntity<?> getAllActiveHospitalsByDzongkhagId(Integer dzongkhagId) {
        List<Hospital> hospitalList = iHospitalRepository.getAllActiveHospitalsByDzongkhagId(dzongkhagId);
        if (hospitalList.size() > 0) {
            return ResponseEntity.ok(hospitalList);
        } else {
            return ResponseEntity.badRequest().body(new MessageResponse("Data not found"));
        }
    }

    @Override
    public ResponseEntity<?> getAllAvailableTimeSlotByHospitalId(Long hospitalId) {
        Collection<HospitalScheduleTime> hospitalScheduleTimes = iHospitalScheduleTimeRepository.findByHospitalIdOrderByAppointmentDateAsc(hospitalId);
        if (hospitalScheduleTimes.size() > 0) {
            return ResponseEntity.ok(hospitalScheduleTimes
                    .stream()
                    .map(mapper::mapToDomain)
                    .collect(Collectors.toUnmodifiableList()));
        } else {
            return ResponseEntity.badRequest().body(new MessageResponse("Data not found"));
        }
    }
}
