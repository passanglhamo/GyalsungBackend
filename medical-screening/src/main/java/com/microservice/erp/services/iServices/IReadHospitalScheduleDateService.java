package com.microservice.erp.services.iServices;

import com.microservice.erp.domain.dto.MedicalConfigurationDto;
import com.microservice.erp.domain.dto.HospitalBookingDetailsDto;
import com.microservice.erp.domain.dto.HospitalScheduleDateDto;
import org.springframework.http.ResponseEntity;

import java.math.BigInteger;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public interface IReadHospitalScheduleDateService {
    Collection<HospitalScheduleDateDto> getAllScheduleDateById(BigInteger dzoHosId);

    ResponseEntity<?> getAllAvailableAppointmentDateByHospitalId(BigInteger hospitalId);

    List<MedicalConfigurationDto> getAllAppointmentDateByHospitalId(Integer hospitalId);

    MedicalConfigurationDto getHospitalBookingDetailByBookingId(String authHeader, Integer hospitalId,
                                                                Date appointmentDate);
}
