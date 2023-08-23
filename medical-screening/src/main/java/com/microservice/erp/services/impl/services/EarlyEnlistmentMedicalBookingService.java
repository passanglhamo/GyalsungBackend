package com.microservice.erp.services.impl.services;

import com.microservice.erp.domain.dto.ApplicationProperties;
import com.microservice.erp.domain.dto.EarlyEnlistmentMedBookingDto;
import com.microservice.erp.domain.dto.EventBusDto;
import com.microservice.erp.domain.dto.UserInfoDto;
import com.microservice.erp.domain.entities.EarlyEnlistmentMedicalBooking;
import com.microservice.erp.domain.mapper.EarlyEnlistmentMedicalBookingMapper;
import com.microservice.erp.domain.repositories.IEarlyEnlistmentMedicalBookingRepository;
import com.microservice.erp.services.HeaderToken;
import com.microservice.erp.services.iServices.IEarlyEnlistmentMedicalBookingService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class EarlyEnlistmentMedicalBookingService implements IEarlyEnlistmentMedicalBookingService {
    private final IEarlyEnlistmentMedicalBookingRepository iEarlyEnlistmentMedicalBookingRepository;
    private final EarlyEnlistmentMedicalBookingMapper mapper;
    private final HeaderToken headerToken;
    private final AddToQueue addToQueue;

    @Autowired
    @Qualifier("userProfileTemplate")
    RestTemplate restTemplate;


    @Override
    public ResponseEntity<?> bookMedicalAppointment(String authHeader, BigInteger currentUserId, EarlyEnlistmentMedBookingDto earlyEnlistmentMedBookingDto) throws Exception {
        //todo: need to call user ms and save user details such as name, cid, gender and dob
        BigInteger applicantUserId = earlyEnlistmentMedBookingDto.getUserId();
        ApplicationContext context = new AnnotationConfigApplicationContext(ApplicationProperties.class);
        ApplicationProperties properties = context.getBean(ApplicationProperties.class);
        HttpEntity<String> httpRequest = headerToken.tokenHeader(authHeader);
        String userUrl = properties.getUserProfileById() + applicantUserId;
        ResponseEntity<UserInfoDto> userResponse = restTemplate.exchange(userUrl, HttpMethod.GET, httpRequest, UserInfoDto.class);

        EarlyEnlistmentMedicalBooking earlyEnlistmentMedicalBookingDb = iEarlyEnlistmentMedicalBookingRepository.findByCid(Objects.requireNonNull(userResponse.getBody()).getCid());

        EarlyEnlistmentMedicalBooking earlyEnlistmentMedicalBooking = new ModelMapper().map(earlyEnlistmentMedBookingDto, EarlyEnlistmentMedicalBooking.class);
        earlyEnlistmentMedicalBooking.setFullName(Objects.requireNonNull(userResponse.getBody()).getFullName());
        earlyEnlistmentMedicalBooking.setDob(Objects.requireNonNull(userResponse.getBody()).getDob());
        earlyEnlistmentMedicalBooking.setCid(Objects.requireNonNull(userResponse.getBody()).getCid());
        earlyEnlistmentMedicalBooking.setGender(Objects.requireNonNull(userResponse.getBody()).getGender());

        if (earlyEnlistmentMedicalBookingDb == null) {
            EarlyEnlistmentMedicalBooking EarlyEnlistmentMedicalBookingId = iEarlyEnlistmentMedicalBookingRepository.findFirstByOrderByHospitalBookingIdDesc();
            BigInteger hospitalBookingId = EarlyEnlistmentMedicalBookingId == null ? BigInteger.ONE : EarlyEnlistmentMedicalBookingId.getHospitalBookingId().add(BigInteger.ONE);
            earlyEnlistmentMedicalBooking.setHospitalBookingId(hospitalBookingId);
            earlyEnlistmentMedicalBooking.setCreatedBy(currentUserId);
            earlyEnlistmentMedicalBooking.setCreatedDate(new Date());

        } else {
            earlyEnlistmentMedicalBooking.setHospitalBookingId(earlyEnlistmentMedicalBookingDb.getHospitalBookingId());
            earlyEnlistmentMedicalBooking.setCreatedBy(earlyEnlistmentMedicalBookingDb.getCreatedBy());
            earlyEnlistmentMedicalBooking.setCreatedDate(earlyEnlistmentMedicalBookingDb.getCreatedDate());
            earlyEnlistmentMedicalBooking.setUpdatedBy(currentUserId);
            earlyEnlistmentMedicalBooking.setUpdatedDate(new Date());
        }
        iEarlyEnlistmentMedicalBookingRepository.save(earlyEnlistmentMedicalBooking);
        sendEmailAndSms(earlyEnlistmentMedBookingDto, userResponse);

        return ResponseEntity.ok(earlyEnlistmentMedBookingDto);

    }

    private void sendEmailAndSms(EarlyEnlistmentMedBookingDto earlyEnlistmentMedBookingDto
            , ResponseEntity<UserInfoDto> userResponse) throws Exception {
        ApplicationContext context = new AnnotationConfigApplicationContext(ApplicationProperties.class);
        ApplicationProperties properties = context.getBean(ApplicationProperties.class);

        Character amPm = earlyEnlistmentMedBookingDto.getAmPm();
        Date appointmentDate = earlyEnlistmentMedBookingDto.getAppointmentDate();
        String hospitalName = earlyEnlistmentMedBookingDto.getHospitalName();
        String morningOrAfternoon = amPm == 'A' ? "morning" : "afternoon";
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM dd, yyyy");
        String formattedAppointmentDate = dateFormat.format(appointmentDate);
        String subject = "Medical Appointment";
        String emailMessage = "Dear " + Objects.requireNonNull(userResponse.getBody()).getFullName() + ",<br></br>" +
                " In continuation of your early enlistment application, the Gyalsung Head Office is pleased to inform you that your application has been reviewed. " +
                " Now you are required to undergo a medical examination. Please report to " + hospitalName + " on " + formattedAppointmentDate + ", in the " + morningOrAfternoon + ".<br>" +
                "We look forward to seeing you in the hospital.</br></br><br></br>" +
                "<small>***This is a system-generated email. Please do not respond to this email.***</small></br></br><br></br>";

        String smsMessage = "Dear " + Objects.requireNonNull(userResponse.getBody()).getFullName() + ",\n" +
                "\n" +
                "In continuation of your early enlistment application, the Gyalsung Head Office is pleased to inform you that your application has been reviewed." +
                " Now you are required to undergo a medical examination. Please report to " + hospitalName + " on " + formattedAppointmentDate + ", in the " + morningOrAfternoon + ".\n" +
                "\n" +
                "We look forward to seeing you in the hospital.\n";


        EventBusDto eventBusSms = EventBusDto.withId(
                null,
                null,
                null,
                smsMessage,
                null,
                Objects.requireNonNull(userResponse.getBody()).getMobileNo(),
                null,
                null);
        EventBusDto eventBusEmail = EventBusDto.withId(
                Objects.requireNonNull(userResponse.getBody()).getEmail(),
                null,
                null,
                emailMessage,
                subject,
                null,
                null,
                null);

        addToQueue.addToQueue("email", eventBusEmail);
        addToQueue.addToQueue("sms", eventBusSms);
    }
}
