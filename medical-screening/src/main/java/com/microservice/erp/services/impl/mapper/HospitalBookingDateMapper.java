package com.microservice.erp.services.impl.mapper;

import com.microservice.erp.domain.dto.HospitalBookingDateDto;
import com.microservice.erp.domain.entities.HospitalBookingDate;
import com.microservice.erp.domain.entities.HospitalBookingDetail;
import com.microservice.erp.domain.repositories.IHospitalBookingDetailsRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
@AllArgsConstructor
public class HospitalBookingDateMapper {
    private final IHospitalBookingDetailsRepository iHospitalBookingDetailsRepository;
    public HospitalBookingDateDto mapToDomain(HospitalBookingDate hospitalBookingDate) {
        List<HospitalBookingDetail> getBookedUserAm = iHospitalBookingDetailsRepository.findAllByHospitalBookingIdAndAmPm(
                hospitalBookingDate.getId(),'A'
        );
        Integer countAmBooked = Objects.isNull(getBookedUserAm)?0:getBookedUserAm.size();

        List<HospitalBookingDetail> getBookedUserPm = iHospitalBookingDetailsRepository.findAllByHospitalBookingIdAndAmPm(
                hospitalBookingDate.getId(),'P'
        );
        Integer countPmBooked = Objects.isNull(getBookedUserPm)?0:getBookedUserPm.size();

        return HospitalBookingDateDto.withId(
                hospitalBookingDate.getId(),
                hospitalBookingDate.getHospitalId(),
                hospitalBookingDate.getAppointmentDate(),
                hospitalBookingDate.getAmSlots(),
                (hospitalBookingDate.getAmSlots()-countAmBooked),
                hospitalBookingDate.getPmSlots(),
                (hospitalBookingDate.getPmSlots()-countPmBooked),
                hospitalBookingDate.getStatus()

        );
    }

}
