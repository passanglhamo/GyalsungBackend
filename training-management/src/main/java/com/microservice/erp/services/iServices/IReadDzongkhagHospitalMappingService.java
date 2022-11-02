package com.microservice.erp.services.iServices;

import com.microservice.erp.domain.entities.DzongkhagHospitalMapping;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface IReadDzongkhagHospitalMappingService {

    List<DzongkhagHospitalMapping> getAllDzongkhagHospitalList();

    DzongkhagHospitalMapping getAllDzongkhagHospitalById(Long id);

    List<DzongkhagHospitalMapping> getAllDzongkhagHosByStatus(String status);

    ResponseEntity<?> getAllActiveHospitalsByDzongkhagId(Integer dzongkhagId);
}
