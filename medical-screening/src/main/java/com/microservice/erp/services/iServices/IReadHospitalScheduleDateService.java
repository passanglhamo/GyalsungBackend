package com.microservice.erp.services.iServices;

import com.microservice.erp.domain.dto.HospitalBookingDateDto;
import com.microservice.erp.domain.dto.HospitalBookingDetailsDto;
import com.microservice.erp.domain.dto.HospitalScheduleDateDto;
import com.microservice.erp.domain.entities.HospitalBookingDate;
import com.microservice.erp.domain.entities.HospitalBookingDetail;
import org.springframework.http.ResponseEntity;

import java.math.BigInteger;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public interface IReadHospitalScheduleDateService {
    Collection<HospitalScheduleDateDto> getAllScheduleDateById(BigInteger dzoHosId);

    ResponseEntity<?> getAllAvailableAppointmentDateByHospitalId(BigInteger hospitalId);

    List<HospitalBookingDateDto> getAllAppointmentDateByHospitalId(BigInteger hospitalId);

    HospitalBookingDateDto getHospitalBookingDetailByBookingId(String authHeader, BigInteger hospitalId,
                                                            Date appointmentDate);

    HospitalBookingDetailsDto getHospitalBookingDetailByUserId(String authHeader, BigInteger userId);
}
