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
    private final AddToQueue addToQueue;


    @Autowired
    @Qualifier("trainingManagementTemplate")
    RestTemplate restTemplate;

    public void allocationAlgorithm(String authHeader, String year, Character gender) {

        List<AllocateEnrollmentTempDto> allocateEnrollmentTempDtoList = new ArrayList<>();
        List<TrainingAcapacitiesDto> trainingAcapacitiesDtos = new ArrayList<>();

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

            String urlAllTraining = properties.getAllAcademy() ;
            ResponseEntity<List<TrainingAcademyDto>> trainingAcademyList = restTemplate.exchange(urlAllTraining, HttpMethod.GET, request, new ParameterizedTypeReference<List<TrainingAcademyDto>>() {
            });

            List<BigInteger> userIds = enrolmentUserInfos
                    .stream()
                    .map(EnrolmentInfo::getUserId)
                    .collect(Collectors.toList());
            List<UserProfileDto> userProfileDtos = userInformationService.getUserInformationByListOfIds(userIds, authHeader);

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
                    Optional<TrainingAcademyDto> trainingAcademy = null;
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
                    if (responseTraining.getBody() != null && (((gender.equals('M') ? Objects.requireNonNull(responseTraining.getBody()).getMaleCapacityAmount() :
                            Objects.requireNonNull(responseTraining.getBody()).getFemaleCapacityAmount())-
                            (enrolmentInfoRepository.getCountByStatusAndGenderAndYearAndTrainingAcademyId(ApprovalStatus.APPROVED.value(),
                                    gender, year, dzongkhagTrainingPreAcaMappings.getFirstPreference()))) - availableCapacity) > 0) {
                         trainingAcademy = Objects.requireNonNull(trainingAcademyList.getBody())
                                .stream()
                                .filter(t -> t.getTrainingAcaId().equals(dzongkhagTrainingPreAcaMappings.getFirstPreference()))
                                .findFirst();
                        allocatedEnroll(enrolledUser.getId(), dzongkhagTrainingPreAcaMappings.getFirstPreference(),
                                allocateEnrollmentTempDtoList, trainingAcapacitiesDtos, gender, year,enrolledUser.getFullName(),
                                enrolledUser.getMobileNo(),enrolledUser.getEmail(),trainingAcademy.get().getName());

                    } else {
                        String urlSecondTraining = properties.getAllTrainingAcaCapById() + year + "&academyId=" + dzongkhagTrainingPreAcaMappings.getSecondPreference();
                        ResponseEntity<TrainingAcademyCapacityDto> responseSecondTraining = restTemplate.exchange(urlSecondTraining, HttpMethod.GET, request, TrainingAcademyCapacityDto.class);
                        int availableSecondCapacity = trainingAcapacitiesDtos.stream()
                                .filter(dto -> dto.getAcademyId().equals(dzongkhagTrainingPreAcaMappings.getSecondPreference())
                                        && (dto.getGender().equals(gender)))
                                .mapToInt(TrainingAcapacitiesDto::getAccommodationNumber)
                                .findFirst()
                                .orElse(0);
                        if (responseTraining.getBody() != null && (((gender.equals('M') ? Objects.requireNonNull(responseSecondTraining.getBody()).getMaleCapacityAmount() :
                                Objects.requireNonNull(responseTraining.getBody()).getFemaleCapacityAmount())-
                                (enrolmentInfoRepository.getCountByStatusAndGenderAndYearAndTrainingAcademyId(ApprovalStatus.APPROVED.value(),
                                        gender, year, dzongkhagTrainingPreAcaMappings.getSecondPreference()))) - availableSecondCapacity) > 0) {

                            trainingAcademy = Objects.requireNonNull(trainingAcademyList.getBody())
                                    .stream()
                                    .filter(t -> t.getTrainingAcaId().equals(dzongkhagTrainingPreAcaMappings.getSecondPreference()))
                                    .findFirst();
                            allocatedEnroll(enrolledUser.getId(), dzongkhagTrainingPreAcaMappings.getSecondPreference(),
                                    allocateEnrollmentTempDtoList, trainingAcapacitiesDtos, gender, year,
                                    enrolledUser.getFullName(),
                                    enrolledUser.getMobileNo(),enrolledUser.getEmail(), trainingAcademy.get().getName());

                        } else {
                            String urlThirdTraining = properties.getAllTrainingAcaCapById() + year + "&academyId=" + dzongkhagTrainingPreAcaMappings.getThirdPreference();
                            ResponseEntity<TrainingAcademyCapacityDto> responseThirdTraining = restTemplate.exchange(urlThirdTraining, HttpMethod.GET, request, TrainingAcademyCapacityDto.class);
                            int availableThirdCapacity = trainingAcapacitiesDtos.stream()
                                    .filter(dto -> dto.getAcademyId().equals(dzongkhagTrainingPreAcaMappings.getThirdPreference())
                                            && (dto.getGender().equals(gender)))
                                    .mapToInt(TrainingAcapacitiesDto::getAccommodationNumber)
                                    .findFirst()
                                    .orElse(0);
                            if (responseTraining.getBody() != null && (((gender.equals('M') ? Objects.requireNonNull(responseThirdTraining.getBody()).getMaleCapacityAmount() :
                                    Objects.requireNonNull(responseTraining.getBody()).getFemaleCapacityAmount())-
                                    (enrolmentInfoRepository.getCountByStatusAndGenderAndYearAndTrainingAcademyId(ApprovalStatus.APPROVED.value(),
                                            gender, year, dzongkhagTrainingPreAcaMappings.getThirdPreference()))) - availableThirdCapacity) > 0) {
                                trainingAcademy = Objects.requireNonNull(trainingAcademyList.getBody())
                                        .stream()
                                        .filter(t -> t.getTrainingAcaId().equals(dzongkhagTrainingPreAcaMappings.getThirdPreference()))
                                        .findFirst();
                                allocatedEnroll(enrolledUser.getId(), dzongkhagTrainingPreAcaMappings.getThirdPreference(),
                                        allocateEnrollmentTempDtoList, trainingAcapacitiesDtos, gender, year,
                                        enrolledUser.getFullName(),
                                        enrolledUser.getMobileNo(),enrolledUser.getEmail(), trainingAcademy.get().getName());

                            } else {
                                String urlFourthTraining = properties.getAllTrainingAcaCapById() + year + "&academyId=" + dzongkhagTrainingPreAcaMappings.getFourthPreference();
                                ResponseEntity<TrainingAcademyCapacityDto> responseFourthTraining = restTemplate.exchange(urlFourthTraining, HttpMethod.GET, request, TrainingAcademyCapacityDto.class);
                                int availableFourthCapacity = trainingAcapacitiesDtos.stream()
                                        .filter(dto -> dto.getAcademyId().equals(dzongkhagTrainingPreAcaMappings.getFourthPreference())
                                                && (dto.getGender().equals(gender)))
                                        .mapToInt(TrainingAcapacitiesDto::getAccommodationNumber)
                                        .findFirst()
                                        .orElse(0);
                                if (responseTraining.getBody() != null && (((gender.equals('M') ? Objects.requireNonNull(responseFourthTraining.getBody()).getMaleCapacityAmount() :
                                        Objects.requireNonNull(responseTraining.getBody()).getFemaleCapacityAmount())-
                                        (enrolmentInfoRepository.getCountByStatusAndGenderAndYearAndTrainingAcademyId(ApprovalStatus.APPROVED.value(),
                                                gender, year, dzongkhagTrainingPreAcaMappings.getFourthPreference()))) - availableFourthCapacity) > 0) {
                                    trainingAcademy = Objects.requireNonNull(trainingAcademyList.getBody())
                                            .stream()
                                            .filter(t -> t.getTrainingAcaId().equals(dzongkhagTrainingPreAcaMappings.getFourthPreference()))
                                            .findFirst();
                                    allocatedEnroll(enrolledUser.getId(), dzongkhagTrainingPreAcaMappings.getFourthPreference(),
                                            allocateEnrollmentTempDtoList, trainingAcapacitiesDtos, gender, year,
                                            enrolledUser.getFullName(),
                                            enrolledUser.getMobileNo(),enrolledUser.getEmail(), trainingAcademy.get().getName());

                                } else {
                                    String urlFifthTraining = properties.getAllTrainingAcaCapById() + year + "&academyId=" + dzongkhagTrainingPreAcaMappings.getFifthPreference();
                                    ResponseEntity<TrainingAcademyCapacityDto> responseFifthTraining = restTemplate.exchange(urlFifthTraining, HttpMethod.GET, request, TrainingAcademyCapacityDto.class);
                                    int availableFifthCapacity = trainingAcapacitiesDtos.stream()
                                            .filter(dto -> dto.getAcademyId().equals(dzongkhagTrainingPreAcaMappings.getFifthPreference())
                                                    && (dto.getGender().equals(gender)))
                                            .mapToInt(TrainingAcapacitiesDto::getAccommodationNumber)
                                            .findFirst()
                                            .orElse(0);
                                    if (responseTraining.getBody() != null && (((gender.equals('M') ? Objects.requireNonNull(responseFifthTraining.getBody()).getMaleCapacityAmount() :
                                            Objects.requireNonNull(responseTraining.getBody()).getFemaleCapacityAmount())-
                                            (enrolmentInfoRepository.getCountByStatusAndGenderAndYearAndTrainingAcademyId(ApprovalStatus.APPROVED.value(),
                                                    gender, year, dzongkhagTrainingPreAcaMappings.getFifthPreference()))) - availableFifthCapacity) > 0) {
                                        trainingAcademy = Objects.requireNonNull(trainingAcademyList.getBody())
                                                .stream()
                                                .filter(t -> t.getTrainingAcaId().equals(dzongkhagTrainingPreAcaMappings.getFifthPreference()))
                                                .findFirst();
                                        allocatedEnroll(enrolledUser.getId(), dzongkhagTrainingPreAcaMappings.getFifthPreference(),
                                                allocateEnrollmentTempDtoList, trainingAcapacitiesDtos, gender, year,
                                                enrolledUser.getFullName(),
                                                enrolledUser.getMobileNo(),enrolledUser.getEmail(), trainingAcademy.get().getName());

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
                allocationMail(allocateEnrollmentTempDtoList,year);
            }


            //To get the user which has been left out after allocation
            List<UserProfileDto> notAllocatedList = userProfileDtos.stream()
                    .filter(dto -> !allocateEnrollmentTempDtoList.stream().anyMatch(otherDto -> otherDto.getUserId().equals(dto.getId())))
                    .sorted(Comparator.comparing(dto -> {
                        EnrolmentInfo user = enrolmentUserInfos.stream()
                                .filter(u -> u.getUserId().equals(dto.getId()))
                                .findFirst()
                                .orElse(null);
                        return (user != null) ? user.getUserId() : null;
                    }))
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
                        .filter(training -> training.getTrainingYear().equals(year) && (
                                ((gender.equals('M') ? training.getMaleCapacityAmount() : (training.getFemaleCapacityAmount())) -
                                        (enrolmentInfoRepository.getCountByStatusAndGenderAndYearAndTrainingAcademyId(ApprovalStatus.APPROVED.value(),
                                                gender, year, training.getAcademyId()))) - (
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
                    Optional<TrainingAcademyDto> trainingAcademy = null;
                    for (TrainingAcademyCapacityDto vacant : vacantAcademy) {
                        int vacantNo = (int) (((gender.equals('M') ? vacant.getMaleCapacityAmount()
                                : vacant.getFemaleCapacityAmount()) -
                                (enrolmentInfoRepository.getCountByStatusAndGenderAndYearAndTrainingAcademyId(ApprovalStatus.APPROVED.value(),
                                        gender, year, vacant.getAcademyId()))) - (trainingAcapacitiesDtos.stream()
                                .filter(trainingAcapacitiesDto -> trainingAcapacitiesDto.getYear().equals(year) &&
                                        (trainingAcapacitiesDto.getAcademyId().equals(vacant.getAcademyId())) && trainingAcapacitiesDto.getGender().equals(gender))
                                .map(TrainingAcapacitiesDto::getAccommodationNumber)
                                .findFirst()
                                .orElse(0)));
                        trainingAcademy = Objects.requireNonNull(trainingAcademyList.getBody())
                                .stream()
                                .filter(t -> t.getTrainingAcaId().equals(vacant.getAcademyId()))
                                .findFirst();
                        for (int i = 1; i <= vacantNo; i++) {
                            UserProfileDto user = notAllocatedList.get(notAllocatedNo);
                            allocatedEnroll(user.getId(), vacant.getAcademyId(),
                                    allocateRemainingEnrollmentTempDtoList, trainingAcapacitiesDtos, gender, year,
                                    user.getFullName(),user.getMobileNo(),user.getEmail(), trainingAcademy.get().getName());
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

                    allocationMail(allocateRemainingEnrollmentTempDtoList,year);
                }

            }

        }


    }

    public void allocatedEnroll(BigInteger userId, Integer trainingAcademyId, List<AllocateEnrollmentTempDto> allocateEnrollmentTempDtoList,
                                List<TrainingAcapacitiesDto> trainingAcapacitiesDtos, Character gender, String year,
                                String fullName, String mobileNo, String email, String fieldSpecName) {
        AllocateEnrollmentTempDto allocateEnrollmentTempDto = new AllocateEnrollmentTempDto();
        allocateEnrollmentTempDto.setUserId(userId);
        allocateEnrollmentTempDto.setAcademyId(trainingAcademyId);
        allocateEnrollmentTempDto.setFullName(fullName);
        allocateEnrollmentTempDto.setMobileNo(mobileNo);
        allocateEnrollmentTempDto.setEmail(email);
        allocateEnrollmentTempDto.setAcademyName(fieldSpecName);
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

    public void allocationMail(List<AllocateEnrollmentTempDto> allocateEnrollmentTempDtoList,String year){
        allocateEnrollmentTempDtoList.forEach(dto -> {

            String message = "Dear " + dto.getFullName() + ", You have been allocated to " +dto.getAcademyName()+ " training academy to undergo Gyalsung training for the year " + year;
            String subject = "Registration Approval";
            EventBus eventBus = EventBus.withId(dto.getEmail(), null, null, message, subject, dto.getMobileNo());

            try {
                addToQueue.addToQueue("email", eventBus);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            try {
                addToQueue.addToQueue("sms", eventBus);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        });
    }
}
