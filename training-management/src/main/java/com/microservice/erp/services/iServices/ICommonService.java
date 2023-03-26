package com.microservice.erp.services.iServices;

import org.springframework.http.ResponseEntity;

public interface ICommonService {
    ResponseEntity<?> getAllDzongkhags();

    ResponseEntity<?> getGeogByDzongkhagId(Integer dzongkhagId);

    ResponseEntity<?> getGeogByGeogId(Integer geogId);

    ResponseEntity<?> getDzongkhagByDzongkhagId(Integer dzongkhagId);

    ResponseEntity<?> getAllHospitals();

    ResponseEntity<?> getHospitalById(Integer hospitalId);

    ResponseEntity<?> getHospitalMappingByHospitalId(Integer hospitalId);

    ResponseEntity<?> getAllTrainingAcademies();

    ResponseEntity<?> getTrainingAcademyById(Integer academyId);

    ResponseEntity<?> getAllActiveHospital();

}
