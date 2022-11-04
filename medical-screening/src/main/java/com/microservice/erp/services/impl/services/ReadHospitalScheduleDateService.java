package com.microservice.erp.services.impl.services;

import com.microservice.erp.domain.dto.HospitalScheduleDateDto;
import com.microservice.erp.domain.entities.HospitalScheduleDate;
import com.microservice.erp.domain.repositories.IHospitalScheduleDateRepository;
import com.microservice.erp.services.helper.MessageResponse;
import com.microservice.erp.services.iServices.IReadHospitalScheduleDateService;
import com.microservice.erp.services.impl.mapper.HospitalScheduleTimeMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReadHospitalScheduleDateService implements IReadHospitalScheduleDateService {

    private final IHospitalScheduleDateRepository repository;
    private final HospitalScheduleTimeMapper mapper;

    public Collection<HospitalScheduleDateDto> getAllScheduleDateById(Long dzoHosId) {
        return repository.findAllByHospitalId(dzoHosId)
                .stream()
                .map(mapper::mapToDomain)
                .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public ResponseEntity<?> getAllAvailableAppointmentDateByHospitalId(Long hospitalId) {
        Collection<HospitalScheduleDate> hospitalScheduleTimes = repository.findByHospitalIdOrderByAppointmentDateAsc(hospitalId);
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
