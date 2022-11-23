package com.microservice.erp.services.iServices;

import com.microservice.erp.domain.dto.HospitalScheduleDateDto;
import org.springframework.http.ResponseEntity;

import java.math.BigInteger;
import java.util.Collection;

public interface IReadHospitalScheduleDateService {
    Collection<HospitalScheduleDateDto> getAllScheduleDateById(BigInteger dzoHosId);

    ResponseEntity<?> getAllAvailableAppointmentDateByHospitalId(Long hospitalId);
}
