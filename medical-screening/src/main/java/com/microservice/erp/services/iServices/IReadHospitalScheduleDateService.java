package com.microservice.erp.services.iServices;

import com.microservice.erp.domain.dto.HospitalScheduleDateDto;
import org.springframework.http.ResponseEntity;

import java.util.Collection;

public interface IReadHospitalScheduleDateService {
    Collection<HospitalScheduleDateDto> getAllScheduleDateById(Long dzoHosId);

    ResponseEntity<?> getAllAvailableAppointmentDateByHospitalId(Long hospitalId);
}
