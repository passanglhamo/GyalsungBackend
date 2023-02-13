package com.microservice.erp.services.impl;

import com.microservice.erp.domain.dao.EnrolmentDao;
import com.microservice.erp.domain.dto.*;
import com.microservice.erp.domain.dto.enrolment.EnrolmentDto;
import com.microservice.erp.domain.entities.EnrolmentInfo;
import com.microservice.erp.domain.entities.RegistrationDateInfo;
import com.microservice.erp.domain.helper.ApprovalStatus;
import com.microservice.erp.domain.helper.MessageResponse;
import com.microservice.erp.domain.mapper.EnrolmentMapper;
import com.microservice.erp.domain.repositories.IEnrolmentInfoRepository;
import com.microservice.erp.domain.repositories.IRegistrationDateInfoRepository;
import com.microservice.erp.services.iServices.IEnrolmentInfoService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.transaction.Transactional;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class EnrolmentInfoService implements IEnrolmentInfoService {

    private final IEnrolmentInfoRepository iEnrolmentInfoRepository;
    private final EnrolmentMapper enrolmentMapper;
    private final IRegistrationDateInfoRepository iRegistrationDateInfoRepository;
    private final EnrolmentDao enrolmentDao;
    private final AddToQueue addToQueue;
    private final DefermentExemptionValidation defermentExemptionValidation;
    private final UserInformationService userInformationService;

    @Autowired
    @Qualifier("userProfileTemplate")
    RestTemplate userRestTemplate;

    @Autowired
    @Qualifier("trainingManagementTemplate")
    RestTemplate restTemplate;

    @Override
    public ResponseEntity<?> getRegistrationDateInfo() {
        //todo remove static code
        RegistrationDateInfo registrationDateInfo = iRegistrationDateInfoRepository.findByStatus('A');
        return ResponseEntity.ok(registrationDateInfo);
    }

    @Override
    public ResponseEntity<?> getMyEnrolmentInfo(BigInteger userId) {
        EnrolmentInfo enrolmentInfo = iEnrolmentInfoRepository.findByUserId(userId);
        if (enrolmentInfo != null) {
            enrolmentInfo.setEnrolmentCoursePreferences(null);
            return ResponseEntity.ok(enrolmentInfo);
        } else {
            return ResponseEntity.badRequest().body("Information not found.");
        }
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public ResponseEntity<?> saveEnrolment(String authHeader, EnrolmentDto enrolmentDto) throws Exception {

        ApplicationContext context = new AnnotationConfigApplicationContext(ApplicationProperties.class);
        ApplicationProperties properties = context.getBean(ApplicationProperties.class);
        //todo need to remove static code
        RegistrationDateInfo registrationDateInfo = iRegistrationDateInfoRepository.findByStatus('A');
        if (registrationDateInfo == null) {
            return ResponseEntity.badRequest().body(new MessageResponse("Registration date information not found."));
        }
        String registrationYear = registrationDateInfo.getRegistrationYear();

        EnrolmentInfo enrolmentInfoDb = iEnrolmentInfoRepository.findByUserId(new BigInteger(String.valueOf(enrolmentDto.getUserId())));
        //to check already enrolled or not
        if (enrolmentInfoDb != null) {
            return ResponseEntity.badRequest().body(new MessageResponse("You have already enrolled."));
        }


        enrolmentDto.setYear(registrationYear);
        enrolmentDto.setStatus(ApprovalStatus.PENDING.value());//P=Pending, D=Deferred, E=Exempted, A=Approved, which means training academy allocated
        enrolmentDto.setEnrolledOn(new Date());

        //todo need to remove static code
        String paramDate = registrationYear + "/12/31";// as on 31st December in the registration date
        BigInteger userId = new BigInteger(String.valueOf(enrolmentDto.getUserId()));
        //check if user is below 18 or not, need to call user service
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", authHeader);
        HttpEntity<String> request = new HttpEntity<>(headers);

        String url = properties.getUserCheckUnderAge() + userId + "&paramDate=" + paramDate;
        ResponseEntity<UserProfileDto> userDtoResponse = userRestTemplate.exchange(url, HttpMethod.GET, request, UserProfileDto.class);
        Integer age = Objects.requireNonNull(userDtoResponse.getBody()).getAge();
        //todo remove static code
        if (age < 18) {//todo: need to set Enum class for minimum age requirement
            enrolmentDto.setUnderAge('Y');
        } else {
            enrolmentDto.setUnderAge('N');
        }
        Character gender = userDtoResponse.getBody().getGender();
        String fullName = userDtoResponse.getBody().getFull_name();
        String mobileNo = userDtoResponse.getBody().getMobile_no();
        String email = userDtoResponse.getBody().getEmail();
        enrolmentDto.setGender(gender);
        var enrolmentInfo = iEnrolmentInfoRepository.save(enrolmentMapper.mapToEntity(enrolmentDto));
        iEnrolmentInfoRepository.save(enrolmentInfo);

        //todo: need to get mail and sms content
        String message = "Dear " + fullName + ",  Thank you for registering to Gyalsung training.";
        String subject = "Gyalsung Registration";

        EventBus eventBus = EventBus.withId(email, null, null, message, subject, mobileNo);

        //todo get from properties
        addToQueue.addToQueue("email", eventBus);
        addToQueue.addToQueue("sms", eventBus);

        return ResponseEntity.ok(new MessageResponse("Enrolled successfully."));
    }

    @Override
    public ResponseEntity<?> getEnrolmentListByYearAndCoursePreference(String authHeader, String year, BigInteger courseId, Integer coursePreferenceNumber) {
        ApplicationContext context = new AnnotationConfigApplicationContext(ApplicationProperties.class);
        ApplicationProperties properties = context.getBean(ApplicationProperties.class);

        List<EnrolmentListDto> enrolmentListDtos = enrolmentDao.getEnrolmentListByYearAndCoursePreference(year, courseId, coursePreferenceNumber);
        if (enrolmentListDtos == null) {
            return ResponseEntity.badRequest().body(new MessageResponse("No information found."));
        }
        List<EnrolmentListDto> enrolmentList = new ArrayList<>();

        List<BigInteger> userIdsVal = enrolmentListDtos
                .stream()
                .map(EnrolmentListDto::getUser_id)
                .collect(Collectors.toList());

        List<UserProfileDto> userProfileDtos = userInformationService.getUserInformationByListOfIds(userIdsVal, authHeader);

        //to get user detail from m-user-service
        enrolmentListDtos.forEach(item -> {
            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", authHeader);
            HttpEntity<String> request = new HttpEntity<>(headers);

            UserProfileDto userProfileDto = userProfileDtos
                    .stream()
                    .filter(user -> item.getUser_id().equals(user.getId()))
                    .findAny()
                    .orElse(null);

            EnrolmentListDto enrolmentListDto = new EnrolmentListDto();
            enrolmentListDto.setUser_id(item.getUser_id());
            enrolmentListDto.setFull_name(userProfileDto.getFullName());
            enrolmentListDto.setCid(userProfileDto.getCid());
            enrolmentListDto.setDob(userProfileDto.getDob());

            enrolmentListDto.setEnrolment_id(item.getEnrolment_id());
            enrolmentListDto.setEnrolled_on(item.getEnrolled_on());
            enrolmentListDto.setRemarks(item.getRemarks());
            enrolmentListDto.setStatus(item.getStatus());
            enrolmentListDto.setTraining_academy_id(item.getTraining_academy_id());
            enrolmentListDto.setYear(item.getYear());
            enrolmentListDto.setPreference_number(item.getPreference_number());
            enrolmentListDto.setCourse_id(item.getCourse_id());

            Integer trainingAcademyId = item.getTraining_academy_id();
            if (trainingAcademyId != null) {
                String urlTraining = properties.getTrainingManAcademyByAcademyId() + trainingAcademyId;
                ResponseEntity<TrainingAcademyDto> responseTraining = restTemplate.exchange(urlTraining, HttpMethod.GET, request, TrainingAcademyDto.class);
                enrolmentListDto.setAcademy_name(Objects.requireNonNull(responseTraining.getBody()).getName());
            }
            BigInteger allocatedCourseId = item.getAllocated_course_id();
            if (allocatedCourseId != null) {
                String urlCourse = properties.getTrainingManCourseByCourceId() + allocatedCourseId;
                ResponseEntity<TrainingAcademyDto> responseCourse = restTemplate.exchange(urlCourse, HttpMethod.GET, request, TrainingAcademyDto.class);
                enrolmentListDto.setCourseName(Objects.requireNonNull(responseCourse.getBody()).getFieldSpecName());
            }

            enrolmentList.add(enrolmentListDto);
        });
        return ResponseEntity.ok(enrolmentList);
    }

    @Override
    @Transactional()
    public ResponseEntity<?> allocateEnrolments(String authHeader, EnrolmentInfoCommand command) throws Exception {
        ApplicationContext context = new AnnotationConfigApplicationContext(ApplicationProperties.class);
        ApplicationProperties properties = context.getBean(ApplicationProperties.class);

        // to check already allocated or not
        for (EnrolmentInfo enrolmentInfo : iEnrolmentInfoRepository.findAllById(command.getEnrolmentIds())) {
            //todo remove static code
            if (enrolmentInfo.getStatus() == 'A') {
                return ResponseEntity.badRequest().body(new MessageResponse("Already allocated training institute."));
            }
        }
        //to save update enrolment info
        iEnrolmentInfoRepository.findAllById(command.getEnrolmentIds()).forEach(item -> {
            if (item.getStatus().equals(ApprovalStatus.PENDING.value())) {
                item.setStatus(ApprovalStatus.APPROVED.value());
                item.setTrainingAcademyId(command.getTrainingAcademyId());
                item.setAllocatedCourseId(command.getAllocatedCourseId());
                iEnrolmentInfoRepository.save(item);
            }

        });
        // to send email and sms
        for (EnrolmentInfo enrolmentInfo : iEnrolmentInfoRepository.findAllById(command.getEnrolmentIds())) {
            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", authHeader);
            HttpEntity<String> request = new HttpEntity<>(headers);
            String academyName = "";
            String courseName = "";

            String userUrl = properties.getUserProfileById() + enrolmentInfo.getUserId();
            ResponseEntity<UserProfileDto> userResponse = userRestTemplate.exchange(userUrl, HttpMethod.GET, request, UserProfileDto.class);
            String fullName = Objects.requireNonNull(userResponse.getBody()).getFullName();
            String mobileNo = Objects.requireNonNull(userResponse.getBody()).getMobileNo();
            String email = Objects.requireNonNull(userResponse.getBody()).getEmail();

            Integer trainingAcademyId = enrolmentInfo.getTrainingAcademyId();
            if (trainingAcademyId != null) {
                String urlTraining = properties.getTrainingManAcademyByAcademyId() + trainingAcademyId;
                ResponseEntity<TrainingAcademyDto> responseTraining = restTemplate.exchange(urlTraining, HttpMethod.GET, request, TrainingAcademyDto.class);
                academyName = Objects.requireNonNull(responseTraining.getBody()).getName();
            }
            BigInteger allocatedCourseId = enrolmentInfo.getAllocatedCourseId();
            if (allocatedCourseId != null) {
                String urlCourse = properties.getTrainingManCourseByCourceId() + allocatedCourseId;
                ResponseEntity<TrainingAcademyDto> responseCourse = restTemplate.exchange(urlCourse, HttpMethod.GET, request, TrainingAcademyDto.class);
                courseName = Objects.requireNonNull(responseCourse.getBody()).getFieldSpecName();
            }
            String message = "Dear " + fullName + ", You have been allocated to " + academyName + " training academy to undergo Gyalsung training in " + courseName + " for the year " + enrolmentInfo.getYear();
            String subject = "Registration Approval";


            EventBus eventBus = EventBus.withId(email, null, null, message, subject, mobileNo);

            addToQueue.addToQueue("email", eventBus);
            addToQueue.addToQueue("sms", eventBus);
        }
        return ResponseEntity.ok(new MessageResponse("Training allocated successfully"));
    }

    @Override
    public ResponseEntity<?> getEnrolmentListByYearCourseAndAcademy(String authHeader, String year, Integer trainingAcademyId, BigInteger courseId) {

        ApplicationContext context = new AnnotationConfigApplicationContext(ApplicationProperties.class);
        ApplicationProperties properties = context.getBean(ApplicationProperties.class);

        List<EnrolmentListDto> enrolmentListDtos = enrolmentDao.getEnrolmentListByYearCourseAndAcademy(year, trainingAcademyId, courseId);
        if (enrolmentListDtos == null) {
            return ResponseEntity.badRequest().body(new MessageResponse("No information found."));
        }
        List<EnrolmentListDto> enrolmentList = new ArrayList<>();
        List<BigInteger> userIdsVal = enrolmentListDtos
                .stream()
                .map(EnrolmentListDto::getUser_id)
                .collect(Collectors.toList());

        List<UserProfileDto> userProfileDtos = userInformationService.getUserInformationByListOfIds(userIdsVal, authHeader);

        //to get user detail from m-user-service
        enrolmentListDtos.forEach(item -> {
            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", authHeader);
            HttpEntity<String> request = new HttpEntity<>(headers);

            UserProfileDto userProfileDto = userProfileDtos
                    .stream()
                    .filter(user -> item.getUser_id().equals(user.getId()))
                    .findAny()
                    .orElse(null);

            EnrolmentListDto enrolmentListDto = new EnrolmentListDto();
            enrolmentListDto.setUser_id(item.getUser_id());
            enrolmentListDto.setFull_name(userProfileDto.getFullName());
            enrolmentListDto.setCid(userProfileDto.getCid());
            enrolmentListDto.setDob(userProfileDto.getDob());

            enrolmentListDto.setEnrolment_id(item.getEnrolment_id());
            enrolmentListDto.setEnrolled_on(item.getEnrolled_on());
            enrolmentListDto.setRemarks(item.getRemarks());
            enrolmentListDto.setStatus(item.getStatus());
            enrolmentListDto.setTraining_academy_id(item.getTraining_academy_id());
            enrolmentListDto.setYear(item.getYear());
            enrolmentListDto.setPreference_number(item.getPreference_number());
            enrolmentListDto.setCourse_id(item.getCourse_id());

            Integer trainingAcaId = item.getTraining_academy_id();
            if (trainingAcademyId != null) {
                String urlTraining = properties.getTrainingManAcademyByAcademyId() + trainingAcaId;
                ResponseEntity<TrainingAcademyDto> responseTraining = restTemplate.exchange(urlTraining, HttpMethod.GET, request, TrainingAcademyDto.class);
                enrolmentListDto.setAcademy_name(Objects.requireNonNull(responseTraining.getBody()).getName());
            }
            BigInteger allocatedCourseId = item.getAllocated_course_id();
            if (allocatedCourseId != null) {
                String urlCourse = properties.getTrainingManCourseByCourceId() + allocatedCourseId;
                ResponseEntity<TrainingAcademyDto> responseCourse = restTemplate.exchange(urlCourse, HttpMethod.GET, request, TrainingAcademyDto.class);
                enrolmentListDto.setCourseName(Objects.requireNonNull(responseCourse.getBody()).getFieldSpecName());
            }

            enrolmentList.add(enrolmentListDto);
        });
        return ResponseEntity.ok(enrolmentList);
    }

    @Override
    public ResponseEntity<?> changeTrainingAcademy(String authHeader, EnrolmentInfoCommand command) throws Exception {

        ApplicationContext context = new AnnotationConfigApplicationContext(ApplicationProperties.class);
        ApplicationProperties properties = context.getBean(ApplicationProperties.class);

        //to save update enrolment info
        iEnrolmentInfoRepository.findAllById(command.getEnrolmentIds()).forEach(item -> {
            //todo remove static
            item.setStatus('A');
            item.setTrainingAcademyId(command.getTrainingAcademyId());
            item.setAllocatedCourseId(command.getAllocatedCourseId());
            iEnrolmentInfoRepository.save(item);
        });

        // to send email and sms
        for (EnrolmentInfo enrolmentInfo : iEnrolmentInfoRepository.findAllById(command.getEnrolmentIds())) {
            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", authHeader);
            HttpEntity<String> request = new HttpEntity<>(headers);
            String academyName = "";
            String courseName = "";

            String userUrl = properties.getUserProfileById() + enrolmentInfo.getUserId();
            ResponseEntity<UserProfileDto> userResponse = userRestTemplate.exchange(userUrl, HttpMethod.GET, request, UserProfileDto.class);
            String fullName = Objects.requireNonNull(userResponse.getBody()).getFullName();
            String mobileNo = Objects.requireNonNull(userResponse.getBody()).getMobileNo();
            String email = Objects.requireNonNull(userResponse.getBody()).getEmail();

            Integer trainingAcademyId = enrolmentInfo.getTrainingAcademyId();
            if (trainingAcademyId != null) {
                String urlTraining = properties.getTrainingManAcademyByAcademyId() + trainingAcademyId;
                ResponseEntity<TrainingAcademyDto> responseTraining = restTemplate.exchange(urlTraining, HttpMethod.GET, request, TrainingAcademyDto.class);
                academyName = Objects.requireNonNull(responseTraining.getBody()).getName();
            }
            BigInteger allocatedCourseId = enrolmentInfo.getAllocatedCourseId();
            if (allocatedCourseId != null) {
                String urlCourse = properties.getTrainingManCourseByCourceId() + allocatedCourseId;
                ResponseEntity<TrainingAcademyDto> responseCourse = restTemplate.exchange(urlCourse, HttpMethod.GET, request, TrainingAcademyDto.class);
                courseName = Objects.requireNonNull(responseCourse.getBody()).getFieldSpecName();
            }
            String message = "Dear " + fullName + ", Your training academy have been changed to " + academyName + " to undergo Gyalsung training in " + courseName + " for the year " + enrolmentInfo.getYear();
            String subject = "Training Academy Change";


            EventBus eventBus = EventBus.withId(email, null, null, message, subject, mobileNo);
            // Todo get from properties
            addToQueue.addToQueue("email", eventBus);
            addToQueue.addToQueue("sms", eventBus);
        }
        return ResponseEntity.ok(new MessageResponse("Training academy changed successfully"));
    }

    @Override
    public ResponseEntity<?> getEnrolmentValidation(BigInteger userId) {
        return defermentExemptionValidation.getDefermentAndExemptValidation(userId);
    }
}
