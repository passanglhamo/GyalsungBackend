package com.microservice.erp.services.impl;

import com.microservice.erp.domain.dao.EnrolmentDao;
import com.microservice.erp.domain.dto.EnrolmentListDto;
import com.microservice.erp.domain.dto.TrainingAcademyDto;
import com.microservice.erp.domain.dto.UserProfileDto;
import com.microservice.erp.domain.dto.enrolment.EnrolmentDto;
import com.microservice.erp.domain.entities.EnrolmentInfo;
import com.microservice.erp.domain.entities.ParentConsentOtp;
import com.microservice.erp.domain.entities.RegistrationDateInfo;
import com.microservice.erp.domain.helper.ApprovalStatus;
import com.microservice.erp.domain.helper.MailSender;
import com.microservice.erp.domain.helper.MessageResponse;
import com.microservice.erp.domain.helper.SmsSender;
import com.microservice.erp.domain.mapper.EnrolmentMapper;
import com.microservice.erp.domain.repositories.IEnrolmentCoursePreferenceRepository;
import com.microservice.erp.domain.repositories.IEnrolmentInfoRepository;
import com.microservice.erp.domain.repositories.IRegistrationDateInfoRepository;
import com.microservice.erp.services.iServices.IEnrolmentInfoService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
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

    private IEnrolmentInfoRepository iEnrolmentInfoRepository;
    private IEnrolmentCoursePreferenceRepository iEnrolmentCoursePreferenceRepository;

    private EnrolmentMapper enrolmentMapper;
    private final IRegistrationDateInfoRepository iRegistrationDateInfoRepository;
    private EnrolmentDao enrolmentDao;

    @Override
    public ResponseEntity<?> getRegistrationDateInfo() {
        RegistrationDateInfo registrationDateInfo = iRegistrationDateInfoRepository.findByStatus('A');
        return ResponseEntity.ok(registrationDateInfo);
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public ResponseEntity<?> saveEnrolment(String authHeader, EnrolmentDto enrolmentDto) throws Exception {
        RegistrationDateInfo registrationDateInfo = iRegistrationDateInfoRepository.findByStatus('A');
        if (registrationDateInfo == null) {
            return ResponseEntity.badRequest().body(new MessageResponse("Registration date information not found."));
        }
        String registrationYear = registrationDateInfo.getRegistrationYear();

        EnrolmentInfo enrolmentInfoDb = iEnrolmentInfoRepository.findByUserId(enrolmentDto.getUserId());
        //to check already enrolled or not
        if (enrolmentInfoDb != null) {
            return ResponseEntity.badRequest().body(new MessageResponse("You have already enrolled."));
        }

        enrolmentDto.setYear(registrationYear);
        enrolmentDto.setStatus('P');//P=Pending, D=Deferred, E=Exempted, A=Approved, which means training academy allocated
        enrolmentDto.setEnrolledOn(new Date());

        String paramDate = registrationYear + "/12/31";// as on 31st December in the registration date
        BigInteger userId = enrolmentDto.getUserId();
        //check if user is below 18 or not, need to call user service
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", authHeader);
        HttpEntity<String> request = new HttpEntity<>(headers);
        String url = "http://localhost:81/api/user/profile/userProfile/checkUnderAge?userId=" + userId + "&paramDate=" + paramDate;
        ResponseEntity<UserProfileDto> userDtoResponse = restTemplate.exchange(url, HttpMethod.GET, request, UserProfileDto.class);
        Integer age = Objects.requireNonNull(userDtoResponse.getBody()).getAge();
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
        SmsSender.sendSms(mobileNo, message);

        String subject = "Gyalsung Registration";
        MailSender.sendMail(email, null, null, message, subject);

        return ResponseEntity.ok(new MessageResponse("Enrolled successfully."));
    }

    @Override
    public ResponseEntity<?> getEnrolmentListByYearAndCoursePreference(String authHeader, String year, BigInteger courseId, Integer coursePreferenceNumber) {
        List<EnrolmentListDto> enrolmentListDtos = enrolmentDao.getEnrolmentListByYearAndCoursePreference(year, courseId, coursePreferenceNumber);
        if (enrolmentListDtos == null) {
            return ResponseEntity.badRequest().body(new MessageResponse("No information found."));
        }
        List<EnrolmentListDto> enrolmentList = new ArrayList<>();
        //to get user detail from m-user-service
        enrolmentListDtos.forEach(item -> {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", authHeader);
            HttpEntity<String> request = new HttpEntity<>(headers);
            String url = "http://localhost:81/api/user/profile/userProfile/getProfileInfo?userId=" + item.getUser_id();
            ResponseEntity<EnrolmentListDto> response = restTemplate.exchange(url, HttpMethod.GET, request, EnrolmentListDto.class);
            EnrolmentListDto enrolmentListDto = new EnrolmentListDto();
            enrolmentListDto.setUser_id(item.getUser_id());
            enrolmentListDto.setFull_name(Objects.requireNonNull(response.getBody()).getFullName());
            enrolmentListDto.setCid(response.getBody().getCid());
            enrolmentListDto.setDob(response.getBody().getDob());

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
                String urlTraining = "http://localhost:8086/api/training/management/common/getTrainingAcademyById?academyId=" + trainingAcademyId;
                ResponseEntity<TrainingAcademyDto> responseTraining = restTemplate.exchange(urlTraining, HttpMethod.GET, request, TrainingAcademyDto.class);
                enrolmentListDto.setAcademy_name(Objects.requireNonNull(responseTraining.getBody()).getName());
            }
            BigInteger allocatedCourseId = item.getAllocated_course_id();
            if (allocatedCourseId != null) {
                String urlCourse = "http://localhost:8086/api/training/management/fieldSpecializations/getCourseByCourseId?courseId=" + allocatedCourseId;
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
        // to check already allocated or not
        for (EnrolmentInfo enrolmentInfo : iEnrolmentInfoRepository.findAllById(command.getEnrolmentIds())) {
            if (enrolmentInfo.getStatus() == 'A') {
                return ResponseEntity.badRequest().body(new MessageResponse("Already allocated training institute."));
            }
        }
        //to save update enrolment info
        iEnrolmentInfoRepository.findAllById(command.getEnrolmentIds()).forEach(item -> {
            item.setStatus('A');
            item.setTrainingAcademyId(command.getTrainingAcademyId());
            item.setAllocatedCourseId(command.getAllocatedCourseId());
            iEnrolmentInfoRepository.save(item);
        });
        // to send email and sms
        for (EnrolmentInfo enrolmentInfo : iEnrolmentInfoRepository.findAllById(command.getEnrolmentIds())) {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", authHeader);
            HttpEntity<String> request = new HttpEntity<>(headers);
            String academyName = "";
            String courseName = "";

            String userUrl = "http://localhost:81/api/user/profile/userProfile/getProfileInfo?userId=" + enrolmentInfo.getUserId();
            ResponseEntity<UserProfileDto> userResponse = restTemplate.exchange(userUrl, HttpMethod.GET, request, UserProfileDto.class);
            String fullName = Objects.requireNonNull(userResponse.getBody()).getFullName();
            String mobileNo = Objects.requireNonNull(userResponse.getBody()).getMobileNo();
            String email = Objects.requireNonNull(userResponse.getBody()).getEmail();

            Integer trainingAcademyId = enrolmentInfo.getTrainingAcademyId();
            if (trainingAcademyId != null) {
                String urlTraining = "http://localhost:8086/api/training/management/common/getTrainingAcademyById?academyId=" + trainingAcademyId;
                ResponseEntity<TrainingAcademyDto> responseTraining = restTemplate.exchange(urlTraining, HttpMethod.GET, request, TrainingAcademyDto.class);
                academyName = Objects.requireNonNull(responseTraining.getBody()).getName();
            }
            BigInteger allocatedCourseId = enrolmentInfo.getAllocatedCourseId();
            if (allocatedCourseId != null) {
                String urlCourse = "http://localhost:8086/api/training/management/fieldSpecializations/getCourseByCourseId?courseId=" + allocatedCourseId;
                ResponseEntity<TrainingAcademyDto> responseCourse = restTemplate.exchange(urlCourse, HttpMethod.GET, request, TrainingAcademyDto.class);
                courseName = Objects.requireNonNull(responseCourse.getBody()).getFieldSpecName();
            }
            String message = "Dear " + fullName + ", You have been allocated to " + academyName + " training academy to undergo Gyalsung training in " + courseName + " for the year " + enrolmentInfo.getYear();
            SmsSender.sendSms(mobileNo, message);

            String subject = "Registration Approval";
            MailSender.sendMail(email, null, null, message, subject);
        }
        return ResponseEntity.ok(new MessageResponse("Training allocated successfully"));
    }
}
