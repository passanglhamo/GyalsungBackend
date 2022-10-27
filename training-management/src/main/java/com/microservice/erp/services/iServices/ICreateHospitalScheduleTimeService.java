package com.microservice.erp.services.iServices;

import com.microservice.erp.domain.dto.HospitalScheduleTimeDto;
import org.springframework.http.ResponseEntity;

import java.io.IOException;

public interface ICreateHospitalScheduleTimeService {
    ResponseEntity<?> saveScheduleTimes(HospitalScheduleTimeDto hospitalScheduleTimeDto) throws IOException;

}
