package com.microservice.erp.services.iServices;

import com.microservice.erp.domain.entities.DzongkhagHospitalMapping;
import org.springframework.http.ResponseEntity;

public interface IUpdateDzongkhagHospitalMappingService {
    ResponseEntity<?> updateDzongkhagHospitalMapping(DzongkhagHospitalMapping dzongkhagHospitalMapping);
}
