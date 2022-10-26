package com.microservice.erp.services.iServices;

import com.microservice.erp.domain.dto.HospitalScheduleTimeListDto;
import org.springframework.http.ResponseEntity;

import java.io.IOException;

public interface IUpdateHospitalScheduleTimeService {
    ResponseEntity<?> updateScheduleTimes(HospitalScheduleTimeListDto hospitalScheduleTimeListDto) throws IOException;
}
