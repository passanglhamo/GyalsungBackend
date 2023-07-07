package com.microservice.erp.services.impl.mapper;

import com.microservice.erp.domain.dto.MedicalConfigurationDto;
import com.microservice.erp.domain.entities.MedicalConfiguration;
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
    public MedicalConfigurationDto mapToDomain(MedicalConfiguration medicalConfiguration) {
        List<HospitalBookingDetail> getBookedUserAm = iHospitalBookingDetailsRepository.findAllByHospitalBookingIdAndAmPm(
                medicalConfiguration.getId(),'A'
        );
        Integer countAmBooked = Objects.isNull(getBookedUserAm)?0:getBookedUserAm.size();

        List<HospitalBookingDetail> getBookedUserPm = iHospitalBookingDetailsRepository.findAllByHospitalBookingIdAndAmPm(
                medicalConfiguration.getId(),'P'
        );
        Integer countPmBooked = Objects.isNull(getBookedUserPm)?0:getBookedUserPm.size();

        return MedicalConfigurationDto.withId(
                medicalConfiguration.getId(),
                medicalConfiguration.getHospitalId(),
                medicalConfiguration.getAppointmentDate(),
                medicalConfiguration.getAmSlots(),
                (medicalConfiguration.getAmSlots()-countAmBooked),
                medicalConfiguration.getPmSlots(),
                (medicalConfiguration.getPmSlots()-countPmBooked),
                medicalConfiguration.getStatus(),
                null

        );
    }

}
