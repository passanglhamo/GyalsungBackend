package com.microservice.erp.services.impl;

import com.microservice.erp.domain.dao.AcademyWiseInfoDao;
import com.microservice.erp.domain.dto.TrainingAcademyDto;
import com.microservice.erp.services.iServices.IAcademyWiseInfoService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class AcademyWiseInfoService implements IAcademyWiseInfoService {

    private AcademyWiseInfoDao academyWiseInfoDao;

    @Override
    public ResponseEntity<?> getEnrolmentFigureByYear(String authHeader, String year) {

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", authHeader);
        HttpEntity<String> request = new HttpEntity<>(headers);
        List<TrainingAcademyDto> trainingAcademyDtos = new ArrayList<>();
        //Todo Need to add discovery url name
        String url = "http://localhost:8086/api/training/management/common/getAllTrainingAcademies";
        ResponseEntity<TrainingAcademyDto[]> responseEntity = restTemplate.exchange(url, HttpMethod.GET, request, TrainingAcademyDto[].class);
        for (TrainingAcademyDto trainingAcademyDto : Objects.requireNonNull(responseEntity.getBody())) {
            BigInteger noOfMaleEnrolled = academyWiseInfoDao.getEnrolmentFigureByYear(year, 'M', trainingAcademyDto.getTrainingAcaId());
            BigInteger noOfFemaleEnrolled = academyWiseInfoDao.getEnrolmentFigureByYear(year, 'F', trainingAcademyDto.getTrainingAcaId());
            trainingAcademyDto.setNoOfMaleEnrolled(noOfMaleEnrolled);
            trainingAcademyDto.setNoOfFemaleEnrolled(noOfFemaleEnrolled);
            trainingAcademyDtos.add(trainingAcademyDto);
        }
        return ResponseEntity.ok(trainingAcademyDtos);
    }
}
