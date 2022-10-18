package com.microservice.erp.services.iServices.hst;

import com.microservice.erp.domain.dto.hst.HospitalScheduleTimeDto;
import org.springframework.http.ResponseEntity;

import java.util.Collection;
import java.util.List;

public interface IReadHospitalScheduleTimeService {
    ResponseEntity<?> getAllDzongkhag();
    Collection<HospitalScheduleTimeDto> getAllScheduleTimesById(Long dzoHosId);

    ResponseEntity<?> getAllActiveHospitalsByDzongkhagId(Integer dzongkhagId);

    ResponseEntity<?> getAllAvailableTimeSlotByHospitalId(Long hospitalId);
}
