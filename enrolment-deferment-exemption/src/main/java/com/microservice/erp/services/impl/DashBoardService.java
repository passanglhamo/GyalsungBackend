package com.microservice.erp.services.impl;

import com.microservice.erp.domain.dao.DashBoardDao;
import com.microservice.erp.domain.dto.*;
import com.microservice.erp.domain.entities.EnrolmentInfo;
import com.microservice.erp.domain.helper.MessageResponse;
import com.microservice.erp.domain.repositories.IEnrolmentInfoRepository;
import com.microservice.erp.services.iServices.IDashBoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
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
import java.util.stream.Collectors;

@Service
public class DashBoardService implements IDashBoardService {
    private final DashBoardDao dashBoardDao;
    private final IEnrolmentInfoRepository iEnrolmentInfoRepository;
    private final UserInformationService userInformationService;

    public DashBoardService(DashBoardDao dashBoardDao, IEnrolmentInfoRepository iEnrolmentInfoRepository, UserInformationService userInformationService) {
        this.dashBoardDao = dashBoardDao;
        this.iEnrolmentInfoRepository = iEnrolmentInfoRepository;
        this.userInformationService = userInformationService;
    }

    @Autowired
    @Qualifier("trainingManagementTemplate")
    RestTemplate restTemplate;

    @Override
    public ResponseEntity<?> getEdeFigure(String year) {
        DashboardDto dashboardDto = dashBoardDao.getEdeFigure(year);
        return ResponseEntity.ok(dashboardDto);
    }

    @Override
    public ResponseEntity<?> getTotalRegisteredList(String authHeader, String year) {
        List<EnrolmentInfo> enrolmentInfos = iEnrolmentInfoRepository.findByYear(year);
        return getUserList(authHeader, enrolmentInfos);
    }

    @Override

    public ResponseEntity<?> getEarlyEnlistmentList(String authHeader, String year) {

        List<EnrolmentInfo> enrolmentInfos = iEnrolmentInfoRepository.findByYearAndUnderAge(year, 'Y');

        return getUserList(authHeader, enrolmentInfos);
    }

    private ResponseEntity<?> getUserList(String authHeader, List<EnrolmentInfo> enrolmentInfos) {
        ApplicationContext context = new AnnotationConfigApplicationContext(ApplicationProperties.class);
        ApplicationProperties properties = context.getBean(ApplicationProperties.class);
        if (enrolmentInfos == null) {
            return ResponseEntity.badRequest().body(new MessageResponse("No information found."));
        }
        List<EnrolmentListDto> enrolmentList = new ArrayList<>();
        List<BigInteger> userIds = enrolmentInfos
                .stream()
                .map(EnrolmentInfo::getUserId)
                .collect(Collectors.toList());

        List<UserProfileDto> userProfileDtos = userInformationService.getUserInformationByListOfIds(userIds, authHeader);
        //to get user detail from m-user-service
        enrolmentInfos.forEach(item -> {
            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", authHeader);
            HttpEntity<String> request = new HttpEntity<>(headers);

            UserProfileDto userProfileDto = userProfileDtos
                    .stream()
                    .filter(user -> item.getUserId().equals(user.getId()))
                    .findAny()
                    .orElse(null);

            EnrolmentListDto enrolmentListDto = new EnrolmentListDto();
            enrolmentListDto.setUser_id(item.getUserId());
            enrolmentListDto.setFull_name(userProfileDto.getFullName());
            enrolmentListDto.setCid(userProfileDto.getCid());
            enrolmentListDto.setDob(userProfileDto.getDob());

            enrolmentListDto.setEnrolment_id(item.getId());
            enrolmentListDto.setEnrolled_on(item.getEnrolledOn());
            enrolmentListDto.setRemarks(item.getRemarks());
            enrolmentListDto.setStatus(item.getStatus());
            enrolmentListDto.setTraining_academy_id(item.getTrainingAcademyId());
            enrolmentListDto.setYear(item.getYear());

            Integer trainingAcaId = item.getTrainingAcademyId();
            if (trainingAcaId != null) {
                String urlTraining = properties.getTrainingManAcademyByAcademyId() + trainingAcaId;
                ResponseEntity<TrainingAcademyDto> responseTraining = restTemplate.exchange(urlTraining, HttpMethod.GET, request, TrainingAcademyDto.class);
                enrolmentListDto.setAcademy_name(Objects.requireNonNull(responseTraining.getBody()).getName());
            }
            BigInteger allocatedCourseId = item.getAllocatedCourseId();
            if (allocatedCourseId != null) {
                String urlCourse = properties.getTrainingManCourseByCourceId() + allocatedCourseId;
                ResponseEntity<TrainingAcademyDto> responseCourse = restTemplate.exchange(urlCourse, HttpMethod.GET, request, TrainingAcademyDto.class);
                enrolmentListDto.setCourseName(Objects.requireNonNull(responseCourse.getBody()).getFieldSpecName());
            }

            enrolmentList.add(enrolmentListDto);
        });
        return ResponseEntity.ok(enrolmentList);
    }
}
