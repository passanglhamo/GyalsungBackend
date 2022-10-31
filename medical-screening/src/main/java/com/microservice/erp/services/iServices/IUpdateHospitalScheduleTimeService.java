package com.microservice.erp.services.iServices;

import com.microservice.erp.domain.dto.HospitalScheduleTimeDto;
import org.springframework.http.ResponseEntity;

import java.io.IOException;

public interface IUpdateHospitalScheduleTimeService {
    ResponseEntity<?> updateScheduleTimes(HospitalScheduleTimeDto hospitalScheduleTimeListDto) throws IOException;
}
