package com.microservice.erp.services.iServices;

import com.microservice.erp.domain.dto.HospitalScheduleTimeDto;
import org.springframework.http.ResponseEntity;

import java.util.Collection;

public interface IReadHospitalScheduleTimeService {
    Collection<HospitalScheduleTimeDto> getAllScheduleTimesById(Long dzoHosId);

    ResponseEntity<?> getAllActiveHospitalsByDzongkhagId(Integer dzongkhagId);

    ResponseEntity<?> getAllAvailableTimeSlotByHospitalId(Long hospitalId);
}
