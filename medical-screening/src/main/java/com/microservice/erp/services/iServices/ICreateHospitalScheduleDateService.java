package com.microservice.erp.services.iServices;

import com.microservice.erp.domain.dto.HospitalScheduleDateDto;
import org.springframework.http.ResponseEntity;

import java.io.IOException;

public interface ICreateHospitalScheduleDateService {
    ResponseEntity<?> saveScheduleDate(HospitalScheduleDateDto hospitalScheduleTimeDto) throws IOException;

}
