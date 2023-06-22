package com.microservice.erp.services.impl.services;

import com.microservice.erp.domain.dto.HospitalScheduleDateDto;
import com.microservice.erp.domain.entities.HospitalBookingDate;
import com.microservice.erp.domain.entities.HospitalScheduleDate;
import com.microservice.erp.domain.repositories.IHospitalBookingDateRepository;
import com.microservice.erp.domain.repositories.IHospitalScheduleDateRepository;
import com.microservice.erp.domain.helper.MessageResponse;
import com.microservice.erp.services.iServices.IReadHospitalScheduleDateService;
import com.microservice.erp.services.impl.mapper.HospitalScheduleTimeMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReadHospitalScheduleDateService implements IReadHospitalScheduleDateService {

    private final IHospitalScheduleDateRepository repository;
    private final IHospitalBookingDateRepository bookingDateRepository;
    private final HospitalScheduleTimeMapper mapper;

    public Collection<HospitalScheduleDateDto> getAllScheduleDateById(BigInteger dzoHosId) {
        return repository.findAllByHospitalId(dzoHosId)
                .stream()
                .map(mapper::mapToDomain)
                .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public ResponseEntity<?> getAllAvailableAppointmentDateByHospitalId(BigInteger hospitalId) {
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

    @Override
    public List<HospitalBookingDate> getAllAppointmentDateByHospitalId(BigInteger hospitalId) {
        return bookingDateRepository.findAllByHospitalId(hospitalId);
    }

    @Override
    public HospitalBookingDate getHospitalBookingDetailByBookingId(String authHeader, BigInteger hospitalId,
                                                                 Date appointmentDate) {
        return bookingDateRepository.findByHospitalIdAndAppointmentDate(hospitalId,appointmentDate);
    }
}
