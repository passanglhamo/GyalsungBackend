package com.microservice.erp.services.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservice.erp.domain.dto.*;
import com.microservice.erp.domain.entities.DzongkhagTrainingPreAcaMapping;
import com.microservice.erp.domain.entities.EnrolmentInfo;
import com.microservice.erp.domain.helper.ApprovalStatus;
import com.microservice.erp.domain.repositories.IDzongkhagTrainingAcaMappingRepository;
import com.microservice.erp.domain.repositories.IEnrolmentInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class AllocationAlgorithm {
    private final IEnrolmentInfoRepository enrolmentInfoRepository;
    private final UserInformationService userInformationService;
    private final IDzongkhagTrainingAcaMappingRepository dzongkhagTrainingAcaMappingRepository;

    @Autowired
    @Qualifier("trainingManagementTemplate")
    RestTemplate restTemplate;

    public void allocationAlgorithm(String authHeader, String year, Character gender) throws JsonProcessingException {

        List<AllocateEnrollmentTempDto> allocateEnrollmentTempDtoList = new ArrayList<>();
        List<TrainingAcapacitiesDto> trainingAcapacitiesDtos = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();

        ApplicationContext context = new AnnotationConfigApplicationContext(ApplicationProperties.class);
        ApplicationProperties properties = context.getBean(ApplicationProperties.class);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", authHeader);
        HttpEntity<String> request = new HttpEntity<>(headers);

        //List of user for the selected year as per the gender selected
        List<EnrolmentInfo> enrolmentUserInfos = enrolmentInfoRepository.findByYearAndStatusAndGenderOrderById(
                year, ApprovalStatus.PENDING.value(), gender
        );

        if (enrolmentUserInfos.size() != 0) {

            List<BigInteger> userIdsMale = enrolmentUserInfos
                    .stream()
                    .map(EnrolmentInfo::getUserId)
                    .collect(Collectors.toList());
            List<UserProfileDto> userProfileDtos = userInformationService.getUserInformationByListOfIds(userIdsMale, authHeader);

            //List of all enrolled user in bhutan
            List<UserProfileDto> enrolledUserInBhutan = userProfileDtos.stream()
                    .filter(dto -> dto.getPresentCountry().equals("Bhutan")) // replace this with your filtering condition
                    .sorted(Comparator.comparing(dto -> {
                        EnrolmentInfo user = enrolmentUserInfos.stream()
                                .filter(u -> u.getUserId().equals(dto.getId()))
                                .findFirst()
                                .orElse(null);
                        return (user != null) ? user.getUserId() : null;
                    }))
                    .collect(Collectors.toList());

            //Allocate user to the training academy
            if (enrolledUserInBhutan.size() != 0) {
                enrolledUserInBhutan.forEach(enrolledUser -> {

                    DzongkhagTrainingPreAcaMapping dzongkhagTrainingPreAcaMappings =
                            dzongkhagTrainingAcaMappingRepository.findByDzongkhagId(enrolledUser.getPresentDzongkhagId());
                    String urlTraining = properties.getAllTrainingAcaCapById() + year + "&academyId=" + dzongkhagTrainingPreAcaMappings.getFirstPreference();
                    int availableCapacity = trainingAcapacitiesDtos.stream()
                            .filter(dto -> dto.getAcademyId().equals(dzongkhagTrainingPreAcaMappings.getFirstPreference())
                                    && (dto.getGender().equals(gender)))
                            .mapToInt(TrainingAcapacitiesDto::getAccommodationNumber)
                            .findFirst()
                            .orElse(0);
                    //Get first preference training capacities
                    ResponseEntity<TrainingAcademyCapacityDto> responseTraining = restTemplate.exchange(urlTraining, HttpMethod.GET, request, TrainingAcademyCapacityDto.class);
                    //allocate user to first preference
                    if (responseTraining.getBody() != null && ((gender.equals('M')?Objects.requireNonNull(responseTraining.getBody()).getMaleCapacityAmount():
                            Objects.requireNonNull(responseTraining.getBody()).getFemaleCapacityAmount()) - availableCapacity) > 0) {
                        allocatedEnroll(enrolledUser.getId(), dzongkhagTrainingPreAcaMappings.getFirstPreference(),
                                allocateEnrollmentTempDtoList, trainingAcapacitiesDtos, gender, year);

                    } else {
                        String urlSecondTraining = properties.getAllTrainingAcaCapById() + year + "&academyId=" + dzongkhagTrainingPreAcaMappings.getSecondPreference();
                        ResponseEntity<TrainingAcademyCapacityDto> responseSecondTraining = restTemplate.exchange(urlSecondTraining, HttpMethod.GET, request, TrainingAcademyCapacityDto.class);
                        int availableSecondCapacity = trainingAcapacitiesDtos.stream()
                                .filter(dto -> dto.getAcademyId().equals(dzongkhagTrainingPreAcaMappings.getSecondPreference())
                                        && (dto.getGender().equals(gender)))
                                .mapToInt(TrainingAcapacitiesDto::getAccommodationNumber)
                                .findFirst()
                                .orElse(0);
                        if (responseTraining.getBody() != null && ((gender.equals('M')?Objects.requireNonNull(responseSecondTraining.getBody()).getMaleCapacityAmount():
                                Objects.requireNonNull(responseTraining.getBody()).getFemaleCapacityAmount()) - availableSecondCapacity) > 0) {
                            allocatedEnroll(enrolledUser.getId(), dzongkhagTrainingPreAcaMappings.getSecondPreference(),
                                    allocateEnrollmentTempDtoList, trainingAcapacitiesDtos, gender, year);

                        } else {
                            String urlThirdTraining = properties.getAllTrainingAcaCapById() + year + "&academyId=" + dzongkhagTrainingPreAcaMappings.getThirdPreference();
                            ResponseEntity<TrainingAcademyCapacityDto> responseThirdTraining = restTemplate.exchange(urlThirdTraining, HttpMethod.GET, request, TrainingAcademyCapacityDto.class);
                            int availableThirdCapacity = trainingAcapacitiesDtos.stream()
                                    .filter(dto -> dto.getAcademyId().equals(dzongkhagTrainingPreAcaMappings.getThirdPreference())
                                            && (dto.getGender().equals(gender)))
                                    .mapToInt(TrainingAcapacitiesDto::getAccommodationNumber)
                                    .findFirst()
                                    .orElse(0);
                            if (responseTraining.getBody() != null && ((gender.equals('M')?Objects.requireNonNull(responseThirdTraining.getBody()).getMaleCapacityAmount():
                                    Objects.requireNonNull(responseTraining.getBody()).getFemaleCapacityAmount()) - availableThirdCapacity) > 0) {
                                allocatedEnroll(enrolledUser.getId(), dzongkhagTrainingPreAcaMappings.getThirdPreference(),
                                        allocateEnrollmentTempDtoList, trainingAcapacitiesDtos, gender, year);

                            } else {
                                String urlFourthTraining = properties.getAllTrainingAcaCapById() + year + "&academyId=" + dzongkhagTrainingPreAcaMappings.getFourthPreference();
                                ResponseEntity<TrainingAcademyCapacityDto> responseFourthTraining = restTemplate.exchange(urlFourthTraining, HttpMethod.GET, request, TrainingAcademyCapacityDto.class);
                                int availableFourthCapacity = trainingAcapacitiesDtos.stream()
                                        .filter(dto -> dto.getAcademyId().equals(dzongkhagTrainingPreAcaMappings.getFourthPreference())
                                                && (dto.getGender().equals(gender)))
                                        .mapToInt(TrainingAcapacitiesDto::getAccommodationNumber)
                                        .findFirst()
                                        .orElse(0);
                                if (responseTraining.getBody() != null && ((gender.equals('M')?Objects.requireNonNull(responseFourthTraining.getBody()).getMaleCapacityAmount():
                                        Objects.requireNonNull(responseTraining.getBody()).getFemaleCapacityAmount()) - availableFourthCapacity) > 0) {
                                    allocatedEnroll(enrolledUser.getId(), dzongkhagTrainingPreAcaMappings.getFourthPreference(),
                                            allocateEnrollmentTempDtoList, trainingAcapacitiesDtos, gender, year);

                                } else {
                                    String urlFifthTraining = properties.getAllTrainingAcaCapById() + year + "&academyId=" + dzongkhagTrainingPreAcaMappings.getFifthPreference();
                                    ResponseEntity<TrainingAcademyCapacityDto> responseFifthTraining = restTemplate.exchange(urlFifthTraining, HttpMethod.GET, request, TrainingAcademyCapacityDto.class);
                                    int availableFifthCapacity = trainingAcapacitiesDtos.stream()
                                            .filter(dto -> dto.getAcademyId().equals(dzongkhagTrainingPreAcaMappings.getFifthPreference())
                                                    && (dto.getGender().equals(gender)))
                                            .mapToInt(TrainingAcapacitiesDto::getAccommodationNumber)
                                            .findFirst()
                                            .orElse(0);
                                    if (responseTraining.getBody() != null && ((gender.equals('M')?Objects.requireNonNull(responseFifthTraining.getBody()).getMaleCapacityAmount():
                                            Objects.requireNonNull(responseTraining.getBody()).getFemaleCapacityAmount()) - availableFifthCapacity) > 0) {
                                        allocatedEnroll(enrolledUser.getId(), dzongkhagTrainingPreAcaMappings.getFifthPreference(),
                                                allocateEnrollmentTempDtoList, trainingAcapacitiesDtos, gender, year);

                                    }
                                }
                            }
                        }
                    }
                });
            }
            List<EnrolmentInfo> updatedEnrolmentInfos = new ArrayList<>();

            if (allocateEnrollmentTempDtoList.size() != 0) {
                allocateEnrollmentTempDtoList.forEach(dto -> {
                    EnrolmentInfo enrolmentInfo = enrolmentInfoRepository.findByUserIdAndYearAndStatus(dto.getUserId(), year, ApprovalStatus.PENDING.value());
                    if (enrolmentInfo != null) {
                        enrolmentInfo.setTrainingAcademyId(dto.getAcademyId());
                        enrolmentInfo.setStatus(ApprovalStatus.APPROVED.value());
                        updatedEnrolmentInfos.add(enrolmentInfo);
                    }
                });
            }

            if (!updatedEnrolmentInfos.isEmpty()) {

                enrolmentInfoRepository.saveAll(updatedEnrolmentInfos);
            }

            String jsonString = objectMapper.writeValueAsString(trainingAcapacitiesDtos);
            HttpEntity<String> requestEntity = new HttpEntity<>(jsonString, headers);

            String changeUrl = properties.getChangeAllocateCapacities();
            restTemplate.exchange(changeUrl, HttpMethod.POST, requestEntity, String.class);

            //To get the user which has been left out after allocation
            List<UserProfileDto> notAllocatedList = userProfileDtos.stream()
                    .filter(dto -> !allocateEnrollmentTempDtoList.stream().anyMatch(otherDto -> otherDto.getUserId().equals(dto.getId())))
                    .collect(Collectors.toList());

            if (notAllocatedList.size() != 0) {
                String urlTraining = properties.getAllTrainingCapacities();
                ParameterizedTypeReference<List<TrainingAcademyCapacityDto>> responseType =
                        new ParameterizedTypeReference<List<TrainingAcademyCapacityDto>>() {
                        };
                ResponseEntity<List<TrainingAcademyCapacityDto>> responseTraining = restTemplate.exchange(urlTraining, HttpMethod.GET, request, responseType);

                //To get list of academies which has vacant sit
                List<TrainingAcademyCapacityDto> vacantAcademy = responseTraining.getBody()
                        .stream()
                        .filter(training -> training.getTrainingYear().equals(year) && ((gender.equals('M') ? training.getMaleCapacityAmount() :
                                training.getFemaleCapacityAmount()) - (
                                trainingAcapacitiesDtos.stream()
                                        .filter(trainingAcapacitiesDto -> trainingAcapacitiesDto.getYear().equals(year) &&
                                                (trainingAcapacitiesDto.getAcademyId().equals(training.getAcademyId())) && trainingAcapacitiesDto.getGender().equals(gender))
                                        .map(TrainingAcapacitiesDto::getAccommodationNumber)
                                        .findFirst()
                                        .orElse(0)

                        )) > 0)
                        .collect(Collectors.toList());

                List<AllocateEnrollmentTempDto> allocateRemainingEnrollmentTempDtoList = new ArrayList<>();

                int notAllocatedNo = 0;
                if (!vacantAcademy.isEmpty()) {
                    for (TrainingAcademyCapacityDto vacant : vacantAcademy) {
                        int vacantNo = (gender.equals('M') ? vacant.getMaleCapacityAmount()
                                : vacant.getFemaleCapacityAmount()) - (trainingAcapacitiesDtos.stream()
                                .filter(trainingAcapacitiesDto -> trainingAcapacitiesDto.getYear().equals(year) &&
                                        (trainingAcapacitiesDto.getAcademyId().equals(vacant.getAcademyId())) && trainingAcapacitiesDto.getGender().equals(gender))
                                .map(TrainingAcapacitiesDto::getAccommodationNumber)
                                .findFirst()
                                .orElse(0));
                        for (int i = 1; i <= vacantNo; i++) {
                            UserProfileDto user = notAllocatedList.get(notAllocatedNo);
                            allocatedEnroll(user.getId(), vacant.getAcademyId(),
                                    allocateRemainingEnrollmentTempDtoList, trainingAcapacitiesDtos, 'M', year);
                            notAllocatedNo++;
                        }
                    }
                }
                List<EnrolmentInfo> updatedRemainingEnrolmentInfos = new ArrayList<>();

                if (allocateRemainingEnrollmentTempDtoList.size() != 0) {
                    allocateRemainingEnrollmentTempDtoList.forEach(dto -> {
                        EnrolmentInfo enrolmentInfo = enrolmentInfoRepository.findByUserIdAndYearAndStatus(dto.getUserId(), year, ApprovalStatus.PENDING.value());
                        if (enrolmentInfo != null) {
                            enrolmentInfo.setTrainingAcademyId(dto.getAcademyId());
                            enrolmentInfo.setStatus(ApprovalStatus.APPROVED.value());
                            updatedRemainingEnrolmentInfos.add(enrolmentInfo);
                        }
                    });
                }

                if (!updatedRemainingEnrolmentInfos.isEmpty()) {

                    enrolmentInfoRepository.saveAll(updatedRemainingEnrolmentInfos);
                }

                String jsonVacantString = objectMapper.writeValueAsString(trainingAcapacitiesDtos);
                HttpEntity<String> requestVacantEntity = new HttpEntity<>(jsonVacantString, headers);

                restTemplate.exchange(changeUrl, HttpMethod.POST, requestVacantEntity, String.class);

            }

        }


    }

    public void allocatedEnroll(BigInteger userId, Integer trainingAcademyId, List<AllocateEnrollmentTempDto> allocateEnrollmentTempDtoList,
                                List<TrainingAcapacitiesDto> trainingAcapacitiesDtos, Character gender, String year) {
        AllocateEnrollmentTempDto allocateEnrollmentTempDto = new AllocateEnrollmentTempDto();
        allocateEnrollmentTempDto.setUserId(userId);
        allocateEnrollmentTempDto.setAcademyId(trainingAcademyId);
        allocateEnrollmentTempDtoList.add(allocateEnrollmentTempDto);

        Optional<TrainingAcapacitiesDto> existingDto = trainingAcapacitiesDtos.stream()
                .filter(dto -> dto.getAcademyId().equals(trainingAcademyId))
                .findFirst();

        if (existingDto.isPresent()) {
            // Update the existing TrainingAcapacitiesDto object
            TrainingAcapacitiesDto dto = existingDto.get();
            dto.setAccommodationNumber(dto.getAccommodationNumber() + 1);
        } else {
            TrainingAcapacitiesDto dto = new TrainingAcapacitiesDto();
            dto.setAcademyId(trainingAcademyId);
            dto.setAccommodationNumber(1);
            dto.setGender(gender);
            dto.setYear(year);
            trainingAcapacitiesDtos.add(dto);
        }


    }
}
