package com.microservice.erp.services.impl;

import com.microservice.erp.domain.dto.*;
import com.microservice.erp.domain.entities.HospitalScheduleDate;
import com.microservice.erp.domain.entities.HospitalScheduleTime;
import com.microservice.erp.domain.entities.MedicalSelfDeclaration;
import com.microservice.erp.domain.helper.MailSender;
import com.microservice.erp.domain.helper.MessageResponse;
import com.microservice.erp.domain.helper.SmsSender;
import com.microservice.erp.domain.repositories.IHospitalScheduleDateRepository;
import com.microservice.erp.domain.repositories.IHospitalScheduleTimeRepository;
import com.microservice.erp.domain.repositories.IMedicalSelfDeclarationRepository;
import com.microservice.erp.services.iServices.IMedicalBookingService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;

@Service
@AllArgsConstructor
public class MedicalBookingService implements IMedicalBookingService {
    private final IHospitalScheduleDateRepository iHospitalScheduleDateRepository;
    private final IHospitalScheduleTimeRepository iHospitalScheduleTimeRepository;
    private final IMedicalSelfDeclarationRepository iMedicalSelfDeclarationRepository;

    @Override
    public ResponseEntity<?> bookMedicalAppointment(String authHeader, MedicalBookingDto medicalBookingDto) throws Exception {
        HospitalScheduleTime hospitalScheduleTimeByUserId = iHospitalScheduleTimeRepository.findByBookedBy(medicalBookingDto.getUserId());
        if (hospitalScheduleTimeByUserId != null) {
            return ResponseEntity.badRequest().body(new MessageResponse("You have already booked an appointment. " +
                    "Go to change appointment and change it if you want to chane your appointment."));
        }
        HospitalScheduleTime hospitalScheduleTimeDb = iHospitalScheduleTimeRepository.findById(medicalBookingDto.getScheduleTimeId()).get();
        HospitalScheduleTime hospitalScheduleTime = new ModelMapper().map(hospitalScheduleTimeDb, HospitalScheduleTime.class);
        hospitalScheduleTime.setBookedBy(medicalBookingDto.getUserId());//todo:need to get userId from CurrentUser Object after completing authorization
        hospitalScheduleTime.setBookedDate(LocalDate.now());
        hospitalScheduleTime.setBookStatus('B');
        iHospitalScheduleTimeRepository.save(hospitalScheduleTime);

        for (MedicalQuestionDto medicalQuestionDto : medicalBookingDto.getMedicalQuestionDtos()) {
            MedicalSelfDeclaration medicalSelfDeclaration = new MedicalSelfDeclaration();
            medicalSelfDeclaration.setUserId(medicalBookingDto.getUserId());//todo:need to get userId from CurrentUser Object after completing authorization
            medicalSelfDeclaration.setMedicalQuestionnaireId(medicalQuestionDto.getMedicalQuestionId());
            medicalSelfDeclaration.setMedicalQuestionName(medicalQuestionDto.getMedicalQuestionName());
            medicalSelfDeclaration.setCheckStatus(medicalQuestionDto.getCheckStatus());
            iMedicalSelfDeclarationRepository.save(medicalSelfDeclaration);
        }
        //to send sms/email
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", authHeader);
        HttpEntity<String> request = new HttpEntity<>(headers);
        String url = "http://localhost:81/api/user/profile/userProfile/getProfileInfo?userId=" + medicalBookingDto.getUserId();
        ResponseEntity<UserInfoDto> userInfoDtoResponse = restTemplate.exchange(url, HttpMethod.GET, request, UserInfoDto.class);

        String hospitalUrl = "http://localhost:81/api/training/management/common/getHospitalById?hospitalId=" + hospitalScheduleTimeDb.getHospitalScheduleDate().getHospitalId();
        ResponseEntity<HospitalDto> hospitalDtoResponse = restTemplate.exchange(hospitalUrl, HttpMethod.GET, request, HospitalDto.class);
        String hospitalName = Objects.requireNonNull(hospitalDtoResponse.getBody()).getHospitalName();

        String dzongkhagUrl = "http://localhost:81/api/training/management/common/getHospitalMappingByHospitalId?hospitalId=" + hospitalScheduleTimeDb.getHospitalScheduleDate().getHospitalId();
        ResponseEntity<DzongkhagDto> dzongkhagDtoResponse = restTemplate.exchange(dzongkhagUrl, HttpMethod.GET, request, DzongkhagDto.class);
        String dzongkhagName = Objects.requireNonNull(dzongkhagDtoResponse.getBody()).getDzongkhagName();

        LocalDate appointmentDate = hospitalScheduleTimeDb.getBookedDate();

        String timeFormat = "HH:mm a";
        //String startTime = hospitalScheduleTimeDb.getStartTime();
        //String endTime = hospitalScheduleTimeDb.getEndTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        SimpleDateFormat sdf1 = new SimpleDateFormat(timeFormat);
        //Date parseDateStartTime = sdf.parse(startTime);
        //Date parseDateEndTime = sdf.parse(endTime);

        //String appointmentStartTime = sdf1.format(parseDateStartTime);
        //String appointmentEndTime = sdf1.format(parseDateEndTime);

//        String message = "Dear " + Objects.requireNonNull(userInfoDtoResponse.getBody()).getFullName() + ", " + "You have booked medical screening appointment for Gyalsung at " + hospitalName + ", " + dzongkhagName + ", " + "on " + appointmentDate + " from " + appointmentStartTime + " to " + appointmentEndTime + ". " + "Please report before 30 minutes on " + appointmentDate;
//        SmsSender.sendSms(Objects.requireNonNull(userInfoDtoResponse.getBody()).getMobileNo(), message);
//        String subject = "Medical Screening Appointment";
//        MailSender.sendMail(userInfoDtoResponse.getBody().getEmail(), null, null, message, subject);

        return ResponseEntity.ok("Appointment booked successfully.");
    }

    @Override
    public ResponseEntity<?> getMedicalAppointmentDetail(String authHeader, Long userId) {
        ResponseDto responseDto = new ResponseDto();
        HospitalScheduleTime hospitalScheduleTime = iHospitalScheduleTimeRepository.findByBookedBy(userId);
        //responseDto.setStartTime(hospitalScheduleTime.getStartTime());
        //responseDto.setEndTime(hospitalScheduleTime.getEndTime());

        HospitalScheduleDate hospitalScheduleDate = iHospitalScheduleDateRepository.getMyBookingDate(userId);
        responseDto.setAppointmentDate(hospitalScheduleDate.getAppointmentDate());

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", authHeader);
        HttpEntity<String> request = new HttpEntity<>(headers);

        String hospitalUrl = "http://localhost:81/api/training/management/common/getHospitalById?hospitalId=" + hospitalScheduleDate.getHospitalId();
        ResponseEntity<HospitalDto> hospitalDtoResponse = restTemplate.exchange(hospitalUrl, HttpMethod.GET, request, HospitalDto.class);
        responseDto.setHospitalName(Objects.requireNonNull(hospitalDtoResponse.getBody()).getHospitalName());

        String dzongkhagUrl = "http://localhost:81/api/training/management/common/getHospitalMappingByHospitalId?hospitalId=" + hospitalScheduleDate.getHospitalId();
        ResponseEntity<DzongkhagDto> dzongkhagDtoResponse = restTemplate.exchange(dzongkhagUrl, HttpMethod.GET, request, DzongkhagDto.class);
        responseDto.setDzongkhagName(Objects.requireNonNull(dzongkhagDtoResponse.getBody()).getDzongkhagName());

        return ResponseEntity.ok(responseDto);
    }

    @Override
    public ResponseEntity<?> changeMedicalAppointment(MedicalBookingDto medicalBookingDto) {
        HospitalScheduleTime hospitalScheduleTimeDbByUserId = iHospitalScheduleTimeRepository.findByBookedBy(medicalBookingDto.getUserId());
        HospitalScheduleTime hospitalScheduleTimeDb = iHospitalScheduleTimeRepository.findById(medicalBookingDto.getScheduleTimeId()).get();

        // to change book status of previous booking to A=Available
        HospitalScheduleTime hospitalScheduleTimeReset = new ModelMapper().map(hospitalScheduleTimeDbByUserId, HospitalScheduleTime.class);
        hospitalScheduleTimeReset.setBookedBy(null);
        hospitalScheduleTimeReset.setBookedDate(null);
        hospitalScheduleTimeReset.setBookStatus('A');
        iHospitalScheduleTimeRepository.save(hospitalScheduleTimeReset);

        HospitalScheduleTime hospitalScheduleTime = new ModelMapper().map(hospitalScheduleTimeDb, HospitalScheduleTime.class);
        hospitalScheduleTime.setBookedBy(medicalBookingDto.getUserId());//todo:need to get userId from CurrentUser Object after completing authorization
        hospitalScheduleTime.setBookedDate(LocalDate.now());
        hospitalScheduleTime.setBookStatus('B');
        iHospitalScheduleTimeRepository.save(hospitalScheduleTime);

        //todo:send sms/email
        return ResponseEntity.ok("Appointment edited successfully.");
    }

    @Override
    public ResponseEntity<?> getPreviousSelfDeclaration(String authHeader, Long userId) {
        List<MedicalSelfDeclaration> medicalSelfDeclarationList = iMedicalSelfDeclarationRepository.findByUserIdOrderByMedicalQuestionNameAsc(userId);
        return ResponseEntity.ok(medicalSelfDeclarationList);
    }

    @Override
    public ResponseEntity<?> resubmitSelfDeclaration(MedicalBookingDto medicalBookingDto) {
        iMedicalSelfDeclarationRepository.deleteAllByUserId(medicalBookingDto.getUserId());
        for (MedicalQuestionDto medicalQuestionDto : medicalBookingDto.getMedicalQuestionDtos()) {
            MedicalSelfDeclaration medicalSelfDeclaration = new MedicalSelfDeclaration();
            medicalSelfDeclaration.setUserId(medicalBookingDto.getUserId());//todo:need to get userId from CurrentUser Object after completing authorization
            medicalSelfDeclaration.setMedicalQuestionnaireId(medicalQuestionDto.getMedicalQuestionId());
            medicalSelfDeclaration.setMedicalQuestionName(medicalQuestionDto.getMedicalQuestionName());
            medicalSelfDeclaration.setCheckStatus(medicalQuestionDto.getCheckStatus());
            iMedicalSelfDeclarationRepository.save(medicalSelfDeclaration);
        }
        return ResponseEntity.ok("Resubmitted successfully.");
    }

}
