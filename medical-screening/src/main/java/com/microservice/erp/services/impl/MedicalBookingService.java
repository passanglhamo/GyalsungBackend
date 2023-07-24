package com.microservice.erp.services.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.microservice.erp.domain.dto.*;
import com.microservice.erp.domain.entities.*;
import com.microservice.erp.domain.helper.MessageResponse;
import com.microservice.erp.domain.repositories.*;
import com.microservice.erp.services.iServices.IMedicalBookingService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class MedicalBookingService implements IMedicalBookingService {
    private final IHospitalScheduleDateRepository iHospitalScheduleDateRepository;
    private final IHospitalScheduleTimeRepository iHospitalScheduleTimeRepository;
    private final IMedicalSelfDeclarationRepository iMedicalSelfDeclarationRepository;
    private final AddToQueueMail addToQueue;

    @Autowired
    @Qualifier("userProfileTemplate")
    RestTemplate userRestTemplate;

    @Autowired
    @Qualifier("trainingManagementTemplate")
    RestTemplate restTemplate;

    @Override
    public ResponseEntity<?> bookMedicalAppointment(String authHeader, MedicalBookingDto medicalBookingDto) throws Exception {

        ApplicationContext context = new AnnotationConfigApplicationContext(ApplicationProperties.class);
        ApplicationProperties properties = context.getBean(ApplicationProperties.class);

        HospitalScheduleTime hospitalScheduleTimeByUserId = iHospitalScheduleTimeRepository.findByBookedBy(medicalBookingDto.getUserId());
        if (hospitalScheduleTimeByUserId != null) {
            return ResponseEntity.badRequest().body(new MessageResponse("You have already booked an appointment. " +
                    "Go to change appointment and change it if you want to change your appointment."));
        }
        HospitalScheduleTime hospitalScheduleTimeDb = iHospitalScheduleTimeRepository.findById(medicalBookingDto.getScheduleTimeId()).get();
        HospitalScheduleTime hospitalScheduleTime = new ModelMapper().map(hospitalScheduleTimeDb, HospitalScheduleTime.class);
        hospitalScheduleTime.setBookedBy(medicalBookingDto.getUserId());//todo:need to get userId from CurrentUser Object after completing authorization
        hospitalScheduleTime.setBookedDate(LocalDate.now());
        //todo remove static
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
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", authHeader);
        HttpEntity<String> request = new HttpEntity<>(headers);
        String url = properties.getUserProfileById() + medicalBookingDto.getUserId();
        ResponseEntity<UserInfoDto> userInfoDtoResponse = userRestTemplate.exchange(url, HttpMethod.GET, request, UserInfoDto.class);

        String hospitalUrl = properties.getTrainingHospitalById() + hospitalScheduleTimeDb.getHospitalScheduleDate().getHospitalId();
        ResponseEntity<HospitalDto> hospitalDtoResponse = restTemplate.exchange(hospitalUrl, HttpMethod.GET, request, HospitalDto.class);
        String hospitalName = Objects.requireNonNull(hospitalDtoResponse.getBody()).getHospitalName();

        String dzongkhagUrl = properties.getTrainingMappedHospitalById() + hospitalScheduleTimeDb.getHospitalScheduleDate().getHospitalId();
        ResponseEntity<DzongkhagDto> dzongkhagDtoResponse = restTemplate.exchange(dzongkhagUrl, HttpMethod.GET, request, DzongkhagDto.class);
        String dzongkhagName = Objects.requireNonNull(dzongkhagDtoResponse.getBody()).getDzongkhagName();

        LocalDate appointmentDate = hospitalScheduleTimeDb.getBookedDate();

        String startTime = hospitalScheduleTimeDb.getStartTime().toString();
        String endTime = hospitalScheduleTimeDb.getEndTime().toString();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        DateFormat timeFormat = new SimpleDateFormat("hh:mm aa");
        Date dateStart = df.parse(startTime);
        Date dateEnd = df.parse(endTime);
        String appointmentStartTime = timeFormat.format(dateStart);
        String appointmentEndTime = timeFormat.format(dateEnd);

        String message = "Dear " + Objects.requireNonNull(userInfoDtoResponse.getBody()).getFullName() + ", " + "You have booked medical screening appointment for Gyalsung at " + hospitalName + ", " + dzongkhagName + ", " + "on " + appointmentDate + " from " + appointmentStartTime + " to " + appointmentEndTime + ". " + "Please report before 30 minutes on " + appointmentDate;
        String subject = "Medical Screening Appointment";

        MailSenderDto eventBus = MailSenderDto.withId(
                Objects.requireNonNull(userInfoDtoResponse.getBody()).getEmail(),
                null,
                null,
                message,
                subject,
                Objects.requireNonNull(userInfoDtoResponse.getBody()).getMobileNo());

        addToQueue.addToQueue("email", eventBus);
        addToQueue.addToQueue("sms", eventBus);
        return ResponseEntity.ok("Appointment booked successfully.");
    }

    @Override
    public ResponseEntity<?> getMedicalAppointmentDetail(String authHeader, BigInteger userId) {
        ApplicationContext context = new AnnotationConfigApplicationContext(ApplicationProperties.class);
        ApplicationProperties properties = context.getBean(ApplicationProperties.class);

        ResponseDto responseDto = new ResponseDto();
        HospitalScheduleTime hospitalScheduleTime = iHospitalScheduleTimeRepository.findByBookedBy(userId);
        responseDto.setStartTime(hospitalScheduleTime.getStartTime());
        responseDto.setEndTime(hospitalScheduleTime.getEndTime());

        HospitalScheduleDate hospitalScheduleDate = iHospitalScheduleDateRepository.getMyBookingDate(userId);
        responseDto.setAppointmentDate(hospitalScheduleDate.getAppointmentDate());

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", authHeader);
        HttpEntity<String> request = new HttpEntity<>(headers);

        String hospitalUrl = properties.getTrainingHospitalById() + hospitalScheduleDate.getHospitalId();
        ResponseEntity<HospitalDto> hospitalDtoResponse = restTemplate.exchange(hospitalUrl, HttpMethod.GET, request, HospitalDto.class);
        responseDto.setHospitalName(Objects.requireNonNull(hospitalDtoResponse.getBody()).getHospitalName());

        String dzongkhagUrl = properties.getTrainingMappedHospitalById() + hospitalScheduleDate.getHospitalId();
        ResponseEntity<DzongkhagDto> dzongkhagDtoResponse = restTemplate.exchange(dzongkhagUrl, HttpMethod.GET, request, DzongkhagDto.class);
        responseDto.setDzongkhagName(Objects.requireNonNull(dzongkhagDtoResponse.getBody()).getDzongkhagName());

        return ResponseEntity.ok(responseDto);
    }

    @Override
    public ResponseEntity<?> changeMedicalAppointment(String authHeader, MedicalBookingDto medicalBookingDto) throws Exception {
        ApplicationContext context = new AnnotationConfigApplicationContext(ApplicationProperties.class);
        ApplicationProperties properties = context.getBean(ApplicationProperties.class);

        HospitalScheduleTime hospitalScheduleTimeDbByUserId = iHospitalScheduleTimeRepository.findByBookedBy(medicalBookingDto.getUserId());
        HospitalScheduleTime hospitalScheduleTimeDb = iHospitalScheduleTimeRepository.findById(medicalBookingDto.getScheduleTimeId()).get();

        // to change book status of previous booking to A=Available
        HospitalScheduleTime hospitalScheduleTimeReset = new ModelMapper().map(hospitalScheduleTimeDbByUserId, HospitalScheduleTime.class);
        hospitalScheduleTimeReset.setBookedBy(null);
        hospitalScheduleTimeReset.setBookedDate(null);
        //todo remove static
        hospitalScheduleTimeReset.setBookStatus('A');
        iHospitalScheduleTimeRepository.save(hospitalScheduleTimeReset);

        HospitalScheduleTime hospitalScheduleTime = new ModelMapper().map(hospitalScheduleTimeDb, HospitalScheduleTime.class);
        hospitalScheduleTime.setBookedBy(medicalBookingDto.getUserId());//todo:need to get userId from CurrentUser Object after completing authorization
        hospitalScheduleTime.setBookedDate(LocalDate.now());
        hospitalScheduleTime.setBookStatus('B');
        iHospitalScheduleTimeRepository.save(hospitalScheduleTime);

        //to send sms/email
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", authHeader);
        HttpEntity<String> request = new HttpEntity<>(headers);
        String url = properties.getUserProfileById() + medicalBookingDto.getUserId();
        ResponseEntity<UserInfoDto> userInfoDtoResponse = userRestTemplate.exchange(url, HttpMethod.GET, request, UserInfoDto.class);

        String hospitalUrl = properties.getTrainingHospitalById() + hospitalScheduleTimeDb.getHospitalScheduleDate().getHospitalId();
        ResponseEntity<HospitalDto> hospitalDtoResponse = restTemplate.exchange(hospitalUrl, HttpMethod.GET, request, HospitalDto.class);
        String hospitalName = Objects.requireNonNull(hospitalDtoResponse.getBody()).getHospitalName();

        String dzongkhagUrl = properties.getTrainingMappedHospitalById() + hospitalScheduleTimeDb.getHospitalScheduleDate().getHospitalId();
        ResponseEntity<DzongkhagDto> dzongkhagDtoResponse = restTemplate.exchange(dzongkhagUrl, HttpMethod.GET, request, DzongkhagDto.class);
        String dzongkhagName = Objects.requireNonNull(dzongkhagDtoResponse.getBody()).getDzongkhagName();

        LocalDate appointmentDate = hospitalScheduleTimeDb.getBookedDate();

        String startTime = hospitalScheduleTimeDb.getStartTime().toString();
        String endTime = hospitalScheduleTimeDb.getEndTime().toString();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        DateFormat timeFormat = new SimpleDateFormat("hh:mm aa");
        Date dateStart = df.parse(startTime);
        Date dateEnd = df.parse(endTime);
        String appointmentStartTime = timeFormat.format(dateStart);
        String appointmentEndTime = timeFormat.format(dateEnd);

        String message = "Dear " + Objects.requireNonNull(userInfoDtoResponse.getBody()).getFullName() + ", " + "You have changed medical screening appointment for Gyalsung at " + hospitalName + ", " + dzongkhagName + ", " + "on " + appointmentDate + " from " + appointmentStartTime + " to " + appointmentEndTime + ". " + "Please report before 30 minutes on " + appointmentDate;
        String subject = "Changed Medical Screening Appointment";

        MailSenderDto mailSenderDto = MailSenderDto.withId(
                Objects.requireNonNull(userInfoDtoResponse.getBody()).getEmail(),
                null,
                null,
                message,
                subject,
                Objects.requireNonNull(userInfoDtoResponse.getBody()).getMobileNo());

        //todo  get from properties
        addToQueue.addToQueue("email", mailSenderDto);
        addToQueue.addToQueue("sms", mailSenderDto);
        return ResponseEntity.ok("Appointment edited successfully.");
    }

    @Override
    public ResponseEntity<?> getPreviousSelfDeclaration(String authHeader, BigInteger userId) {
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
