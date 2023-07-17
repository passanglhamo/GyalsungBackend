package com.microservice.erp.services.impl;

import com.microservice.erp.domain.dao.DashBoardDao;
import com.microservice.erp.domain.dto.*;
import com.microservice.erp.domain.entities.DefermentInfo;
import com.microservice.erp.domain.entities.ExemptionInfo;
import com.microservice.erp.domain.helper.ApprovalStatus;
import com.microservice.erp.domain.helper.MessageResponse;
import com.microservice.erp.domain.repositories.*;
import com.microservice.erp.services.iServices.IDashBoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.ParameterizedTypeReference;
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
    private final UserInformationService userInformationService;
    private final IDefermentInfoRepository iDefermentInfoRepository;
    private final IExemptionInfoRepository iExemptionInfoRepository;

    public DashBoardService(DashBoardDao dashBoardDao,  UserInformationService userInformationService, IDefermentInfoRepository iDefermentInfoRepository, IExemptionInfoRepository iExemptionInfoRepository) {
        this.dashBoardDao = dashBoardDao;
        this.userInformationService = userInformationService;
        this.iDefermentInfoRepository = iDefermentInfoRepository;
        this.iExemptionInfoRepository = iExemptionInfoRepository;
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
    public ResponseEntity<?> getRegisteredList(String authHeader, String year) {
//        List<EnrolmentInfo> enrolmentInfos = iEnrolmentInfoRepository.findByYear(year);
        return getUserList(authHeader);
    }

    @Override

    public ResponseEntity<?> getEarlyEnlistmentList(String authHeader, String year) {
//        List<EnrolmentInfo> enrolmentInfos = iEnrolmentInfoRepository.findByYearAndUnderAge(year, 'Y');
        return getUserList(authHeader);
    }

    @Override
    public ResponseEntity<?> getDeferredList(String authHeader, String year) {
        Character status = ApprovalStatus.APPROVED.value();
        List<DefermentInfo> defermentInfos = iDefermentInfoRepository.findByDefermentYearAndStatus(year, status);
        List<BigInteger> userIds = defermentInfos
                .stream()
                .map(DefermentInfo::getUserId)
                .collect(Collectors.toList());
        List<UserProfileDto> userProfileDtos = userInformationService.getUserInformationByListOfIds(userIds, authHeader);
        List<DefermentDto> defermentDtoList = new ArrayList<>();

        userProfileDtos.forEach(userProfileDto -> {
            DefermentInfo defermentInfo = defermentInfos
                    .stream()
                    .filter(deferment -> deferment.getUserId().equals(userProfileDto.getId()))
                    .findAny()
                    .orElse(null);
            if (defermentInfo != null) {
                DefermentDto defermentDto = new DefermentDto();
                defermentDto.setFullName(userProfileDto.getFullName());
                defermentDto.setCid(userProfileDto.getCid());
                defermentDto.setDob(userProfileDto.getDob());
                defermentDto.setGender(userProfileDto.getGender());
                defermentDto.setDefermentYear(defermentInfo.getDefermentYear());
                defermentDto.setId(defermentInfo.getDefermentId());
                defermentDtoList.add(defermentDto);
            }

        });
        return ResponseEntity.ok(defermentDtoList);
    }

    @Override
    public ResponseEntity<?> getExemptedList(String authHeader, String year) {
        //todo:get exempted list
        Character status = ApprovalStatus.APPROVED.value();
        List<ExemptionInfo> exemptionInfos = iExemptionInfoRepository.findByExemptionYearAndStatus(year, status);
        List<BigInteger> userIds = exemptionInfos
                .stream()
                .map(ExemptionInfo::getUserId)
                .collect(Collectors.toList());
        List<UserProfileDto> userProfileDtos = userInformationService.getUserInformationByListOfIds(userIds, authHeader);
        List<ExemptionDto> exemptionDtoList = new ArrayList<>();

        userProfileDtos.forEach(userProfileDto -> {
            ExemptionInfo exemptionInfo = exemptionInfos
                    .stream()
                    .filter(exemption -> exemption.getUserId().equals(userProfileDto.getId()))
                    .findAny()
                    .orElse(null);

            if (exemptionInfo != null) {
                ExemptionDto exemptionDto = new ExemptionDto();
                exemptionDto.setFullName(userProfileDto.getFullName());
                exemptionDto.setCid(userProfileDto.getCid());
                exemptionDto.setDob(userProfileDto.getDob());
                exemptionDto.setGender(userProfileDto.getGender());
                exemptionDto.setExemptionYear(exemptionInfo.getExemptionYear());
                exemptionDto.setId(exemptionInfo.getExemptionId());
                exemptionDtoList.add(exemptionDto);
            }

        });
        return ResponseEntity.ok(exemptionDtoList);
    }

    @Override
    public ResponseEntity<?> getAcademyWiseEnrolmentFigure(String authHeader, String year) {
        ApplicationContext context = new AnnotationConfigApplicationContext(ApplicationProperties.class);
        ApplicationProperties properties = context.getBean(ApplicationProperties.class);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", authHeader);
        HttpEntity<String> request = new HttpEntity<>(headers);

        String urlTraining = properties.getAllAcademy();
        ResponseEntity<List<TrainingAcademyDto>> trainingAcademyList = restTemplate.exchange(urlTraining, HttpMethod.GET, request, new ParameterizedTypeReference<List<TrainingAcademyDto>>() {
        });
        List<TrainingAcademyWiseEnrolmentDto> trainingAcademyWiseEnrolmentDtos = new ArrayList<>();
        for (TrainingAcademyDto trainingAcademyDto : trainingAcademyList.getBody()) {
            TrainingAcademyWiseEnrolmentDto trainingAcademyWiseEnrolmentDto = new TrainingAcademyWiseEnrolmentDto();
            Integer trainingAcademyId = trainingAcademyDto.getTrainingAcaId();
            Character status = ApprovalStatus.APPROVED.value();
            Character male = 'M';
            Character female = 'F';
//            List<EnrolmentInfo> enrolmentInfoMale = iEnrolmentInfoRepository.findByYearAndTrainingAcademyIdAndStatusAndGender(year, trainingAcademyId, status, male);
//            List<EnrolmentInfo> enrolmentInfoFemale = iEnrolmentInfoRepository.findByYearAndTrainingAcademyIdAndStatusAndGender(year, trainingAcademyId, status, female);

//            trainingAcademyWiseEnrolmentDto.setNoOfMaleEnrolled(enrolmentInfoMale.size());
//            trainingAcademyWiseEnrolmentDto.setNoOfFemaleEnrolled(enrolmentInfoFemale.size());
            trainingAcademyWiseEnrolmentDto.setTrainingAcademyName(trainingAcademyDto.getName());
            trainingAcademyWiseEnrolmentDtos.add(trainingAcademyWiseEnrolmentDto);
        }
        return ResponseEntity.ok(trainingAcademyWiseEnrolmentDtos);
    }

    @Override
    public ResponseEntity<?> getTaskStatusByYear(String authHeader, String year) {
        TaskStatusDto taskStatusDto = dashBoardDao.getTaskStatusByYear(year);
        return ResponseEntity.ok(taskStatusDto);
    }

    private ResponseEntity<?> getUserList(String authHeader) {
        ApplicationContext context = new AnnotationConfigApplicationContext(ApplicationProperties.class);
        ApplicationProperties properties = context.getBean(ApplicationProperties.class);
//        if (enrolmentInfos == null) {
//            return ResponseEntity.badRequest().body(new MessageResponse("No information found."));
//        }
//        List<EnrolmentListDto> enrolmentList = new ArrayList<>();
//        List<BigInteger> userIds = enrolmentInfos
//                .stream()
//                .map(EnrolmentInfo::getUserId)
//                .collect(Collectors.toList());
//
//        List<UserProfileDto> userProfileDtos = userInformationService.getUserInformationByListOfIds(userIds, authHeader);
//        //to get user detail from m-user-service
//        enrolmentInfos.forEach(item -> {
//            HttpHeaders headers = new HttpHeaders();
//            headers.add("Authorization", authHeader);
//            HttpEntity<String> request = new HttpEntity<>(headers);
//
//            UserProfileDto userProfileDto = userProfileDtos
//                    .stream()
//                    .filter(user -> item.getUserId().equals(user.getId()))
//                    .findAny()
//                    .orElse(null);
//
//            EnrolmentListDto enrolmentListDto = new EnrolmentListDto();
//            enrolmentListDto.setUser_id(item.getUserId());
//            enrolmentListDto.setFull_name(userProfileDto.getFullName());
//            enrolmentListDto.setCid(userProfileDto.getCid());
//            enrolmentListDto.setDob(userProfileDto.getDob());
//
//            enrolmentListDto.setEnrolment_id(item.getId());
//            enrolmentListDto.setEnrolled_on(item.getEnrolledOn());
//            enrolmentListDto.setRemarks(item.getRemarks());
//            enrolmentListDto.setStatus(item.getStatus());
//            enrolmentListDto.setTraining_academy_id(item.getTrainingAcademyId());
//            enrolmentListDto.setYear(item.getYear());
//
//            Integer trainingAcaId = item.getTrainingAcademyId();
//            if (trainingAcaId != null) {
//                String urlTraining = properties.getTrainingManAcademyByAcademyId() + trainingAcaId;
//                ResponseEntity<TrainingAcademyDto> responseTraining = restTemplate.exchange(urlTraining, HttpMethod.GET, request, TrainingAcademyDto.class);
//                enrolmentListDto.setAcademy_name(Objects.requireNonNull(responseTraining.getBody()).getName());
//            }
//            BigInteger allocatedCourseId = item.getAllocatedCourseId();
//            if (allocatedCourseId != null) {
//                String urlCourse = properties.getTrainingManCourseByCourseId() + allocatedCourseId;
//                ResponseEntity<TrainingAcademyDto> responseCourse = restTemplate.exchange(urlCourse, HttpMethod.GET, request, TrainingAcademyDto.class);
//                enrolmentListDto.setCourseName(Objects.requireNonNull(responseCourse.getBody()).getFieldSpecName());
//            }

//            enrolmentList.add(enrolmentListDto);
//        });
        return ResponseEntity.ok("");
    }

}
