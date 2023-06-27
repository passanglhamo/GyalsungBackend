package com.microservice.erp.domain.mapper;

import com.microservice.erp.domain.dto.EarlyEnlistmentMedBookingDto;
import com.microservice.erp.domain.entities.EarlyEnlistmentMedicalBooking;
import com.microservice.erp.domain.repositories.IEarlyEnlistmentMedicalBookingRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.Date;

@Component
@AllArgsConstructor
public class EarlyEnlistmentMedicalBookingMapper {
    private final IEarlyEnlistmentMedicalBookingRepository repository;

    public EarlyEnlistmentMedicalBooking mapToEntity(EarlyEnlistmentMedBookingDto earlyEnlistmentMedBookingDto) {
        EarlyEnlistmentMedicalBooking earlyEnlistmentMedicalBooking = new ModelMapper().map(earlyEnlistmentMedBookingDto, EarlyEnlistmentMedicalBooking.class);
        EarlyEnlistmentMedicalBooking earlyEnlistmentMedicalBookingData = repository.findFirstByOrderByHospitalBookingIdDesc();
        BigInteger bookingId = earlyEnlistmentMedicalBookingData == null ? BigInteger.ONE : earlyEnlistmentMedicalBookingData.getHospitalBookingId().add(BigInteger.ONE);
        assert earlyEnlistmentMedicalBooking != null;
        earlyEnlistmentMedicalBooking.setHospitalBookingId(bookingId);
        earlyEnlistmentMedicalBooking.setCreatedBy(earlyEnlistmentMedBookingDto.getUserId());
        earlyEnlistmentMedicalBooking.setCreatedDate(new Date());
        return earlyEnlistmentMedicalBooking;
    }
}
