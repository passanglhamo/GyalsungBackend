package com.microservice.erp.services.iServices.hst;

import com.microservice.erp.domain.dto.hst.HospitalScheduleTimeDto;
import org.springframework.http.ResponseEntity;

import java.io.IOException;

public interface ICreateHospitalScheduleTimeService {
    ResponseEntity<?> save(HospitalScheduleTimeDto hospitalScheduleTimeDto) throws IOException;

}
