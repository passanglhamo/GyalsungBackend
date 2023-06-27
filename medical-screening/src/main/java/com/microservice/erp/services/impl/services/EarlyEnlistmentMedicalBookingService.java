package com.microservice.erp.services.impl.services;

import com.microservice.erp.domain.dto.EarlyEnlistmentMedBookingDto;
import com.microservice.erp.domain.mapper.EarlyEnlistmentMedicalBookingMapper;
import com.microservice.erp.domain.repositories.IEarlyEnlistmentMedicalBookingRepository;
import com.microservice.erp.services.iServices.IEarlyEnlistmentMedicalBookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.Date;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class EarlyEnlistmentMedicalBookingService implements IEarlyEnlistmentMedicalBookingService {
    private final IEarlyEnlistmentMedicalBookingRepository repository;
    private final EarlyEnlistmentMedicalBookingMapper mapper;

    @Override
    public ResponseEntity<?> save(EarlyEnlistmentMedBookingDto earlyEnlistmentMedBookingDto) {
        if(Objects.isNull(earlyEnlistmentMedBookingDto.getHospitalBookingId())){
            var earlyEnlistmentMedicalBooking = repository.save(
                    mapper.mapToEntity(
                            earlyEnlistmentMedBookingDto
                    )
            );

            var medicalBooking = repository.save(earlyEnlistmentMedicalBooking);
            return ResponseEntity.ok(medicalBooking.getHospitalBookingId());
        }

        repository.findById(earlyEnlistmentMedBookingDto.getHospitalBookingId()).ifPresent(d -> {
            d.setHospitalId(earlyEnlistmentMedBookingDto.getHospitalId());
            d.setAppointmentDate(earlyEnlistmentMedBookingDto.getAppointmentDate());
            d.setAmPm(earlyEnlistmentMedBookingDto.getAmPm());
            d.setCreatedBy(earlyEnlistmentMedBookingDto.getUserId());
            d.setCreatedDate(new Date());
            repository.save(d);
        });

        return ResponseEntity.ok("Medical Booking updated successfully.");

    }

    @Override
    public ResponseEntity<?> getEarlyEnlistMedBookingByUserId(BigInteger userId, BigInteger earlyEnlistmentId) {
        return ResponseEntity.ok(repository.findByEarlyEnlistmentIdAndUserId(earlyEnlistmentId, userId));
    }
}
