package com.microservice.erp.services.iServices.hst;

import com.microservice.erp.domain.dto.hst.HospitalScheduleTimeListDto;
import org.springframework.http.ResponseEntity;

import java.io.IOException;

public interface IUpdateHospitalScheduleTimeService {
    ResponseEntity<?> updateScheduleTimes(HospitalScheduleTimeListDto hospitalScheduleTimeListDto) throws IOException;
}
