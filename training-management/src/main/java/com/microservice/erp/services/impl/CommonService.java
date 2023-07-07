package com.microservice.erp.services.impl;

import com.microservice.erp.domain.entities.Dzongkhag;
import com.microservice.erp.domain.entities.Geog;
import com.microservice.erp.domain.entities.Hospital;
import com.microservice.erp.domain.entities.TrainingAcademy;
import com.microservice.erp.domain.repository.IDzongkhagRepository;
import com.microservice.erp.domain.repository.IGeogRepository;
import com.microservice.erp.domain.repository.IHospitalRepository;
import com.microservice.erp.domain.repository.ITrainingAcademyRepository;
import com.microservice.erp.services.iServices.ICommonService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CommonService implements ICommonService {

    private IDzongkhagRepository dzongkhagRepository;
    private IHospitalRepository hospitalRepository;
    private IGeogRepository geogRepository;
    private ITrainingAcademyRepository trainingAcademyRepository;

    @Override
    public ResponseEntity<?> getAllDzongkhags() {
        List<Dzongkhag> dzongkhags = dzongkhagRepository.findAllByOrderByDzongkhagNameAsc();
        return ResponseEntity.ok(dzongkhags);
    }

    @Override
    public ResponseEntity<?> getGeogByDzongkhagId(Integer dzongkhagId) {
        List<Geog> geogs = geogRepository.findByDzongkhagIdOrderByGeogNameAsc(dzongkhagId);
        return ResponseEntity.ok(geogs);
    }

    @Override
    public ResponseEntity<?> getGeogByGeogId(Integer geogId) {
        Geog geog = geogRepository.findByGeogId(geogId);
        return ResponseEntity.ok(geog);
    }

    @Override
    public ResponseEntity<?> getDzongkhagByDzongkhagId(Integer dzongkhagId) {
        Dzongkhag dzongkhag = dzongkhagRepository.findByDzongkhagId(dzongkhagId);
        return ResponseEntity.ok(dzongkhag);
    }

    @Override
    public ResponseEntity<?> getAllHospitals() {
        List<Hospital> hospitals = hospitalRepository.findAllByOrderByHospitalNameAsc();
        return ResponseEntity.ok(hospitals);
    }

    @Override
    public ResponseEntity<?> getAllActiveHospital() {
        List<Hospital> hospitals = hospitalRepository.findAllByStatusOrderByHospitalNameAsc('A');
        return ResponseEntity.ok(hospitals);
    }

    @Override
    public ResponseEntity<?> getHospitalById(Integer hospitalId) {
        Hospital hospital = hospitalRepository.findByHospitalId(hospitalId);
        return ResponseEntity.ok(hospital);
    }

    @Override
    public ResponseEntity<?> getHospitalMappingByHospitalId(Integer hospitalId) {
        Dzongkhag dzongkhag = dzongkhagRepository.getHospitalMappingByHospitalId(hospitalId);
        return ResponseEntity.ok(dzongkhag);
    }

    @Override
    public ResponseEntity<?> getAllTrainingAcademies() {
        List<TrainingAcademy> trainingAcademies = trainingAcademyRepository.findAllByOrderByNameAsc();
        return ResponseEntity.ok(trainingAcademies);
    }

    @Override
    public ResponseEntity<?> getTrainingAcademyById(Integer academyId) {
        TrainingAcademy trainingAcademy = trainingAcademyRepository.findById(academyId).get();
        return ResponseEntity.ok(trainingAcademy);
    }

    @Override
    public ResponseEntity<?> getHospitalByName(String hospitalName) {
        return ResponseEntity.ok(hospitalRepository.findByHospitalName(hospitalName));
    }

}
