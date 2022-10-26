package com.microservice.erp.services.iServices;

import com.microservice.erp.domain.entities.DzongkhagHospitalMapping;

import java.util.List;

public interface IReadDzongkhagHospitalMappingService {

    List<DzongkhagHospitalMapping> findAll();

    DzongkhagHospitalMapping findById(Long id);
}
