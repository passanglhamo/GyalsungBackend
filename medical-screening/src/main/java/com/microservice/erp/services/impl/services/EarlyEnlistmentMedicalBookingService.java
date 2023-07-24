package com.microservice.erp.services.impl.services;

import com.microservice.erp.domain.dto.ApplicationProperties;
import com.microservice.erp.domain.dto.EarlyEnlistmentMedBookingDto;
import com.microservice.erp.domain.dto.EventBusDto;
import com.microservice.erp.domain.dto.UserInfoDto;
import com.microservice.erp.domain.mapper.EarlyEnlistmentMedicalBookingMapper;
import com.microservice.erp.domain.repositories.IEarlyEnlistmentMedicalBookingRepository;
import com.microservice.erp.services.HeaderToken;
import com.microservice.erp.services.iServices.IEarlyEnlistmentMedicalBookingService;
import lombok.RequiredArgsConstructor;
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
    private final IEarlyEnlistmentMedicalBookingRepository repository;
    private final EarlyEnlistmentMedicalBookingMapper mapper;
    private final HeaderToken headerToken;
    private final AddToQueue addToQueue;

    @Autowired
    @Qualifier("userProfileTemplate")
    RestTemplate restTemplate;


    @Override
    public ResponseEntity<?> save(String authHeader, EarlyEnlistmentMedBookingDto earlyEnlistmentMedBookingDto) {
        if (Objects.isNull(earlyEnlistmentMedBookingDto.getHospitalBookingId())) {
            var earlyEnlistmentMedicalBooking = repository.save(
                    mapper.mapToEntity(
                            earlyEnlistmentMedBookingDto
                    )
            );

            var medicalBooking = repository.save(earlyEnlistmentMedicalBooking);
            earlyEnlistmentMedBookingDto.setHospitalBookingId(medicalBooking.getHospitalBookingId());
            try {
                sendEmailAndSms(authHeader, earlyEnlistmentMedBookingDto.getUserId(), earlyEnlistmentMedBookingDto.getAppointmentDate(),
                        earlyEnlistmentMedBookingDto.getHospitalName(),new Date());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            return ResponseEntity.ok(earlyEnlistmentMedBookingDto);
        }else{
            repository.findById(earlyEnlistmentMedBookingDto.getHospitalBookingId()).ifPresent(d -> {
                d.setHospitalId(earlyEnlistmentMedBookingDto.getHospitalId());
                d.setAppointmentDate(earlyEnlistmentMedBookingDto.getAppointmentDate());
                d.setAmPm(earlyEnlistmentMedBookingDto.getAmPm());
                d.setCreatedBy(earlyEnlistmentMedBookingDto.getUserId());
                d.setCreatedDate(new Date());
                earlyEnlistmentMedBookingDto.setHospitalBookingId(d.getHospitalBookingId());
                repository.save(d);
            });
        }

        try {
            sendEmailAndSms(authHeader, earlyEnlistmentMedBookingDto.getUserId(), earlyEnlistmentMedBookingDto.getAppointmentDate(),
                    earlyEnlistmentMedBookingDto.getHospitalName(), new Date());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return ResponseEntity.ok(earlyEnlistmentMedBookingDto);

    }

    @Override
    public ResponseEntity<?> getEarlyEnlistMedBookingByUserId(BigInteger userId, BigInteger earlyEnlistmentId) {
        return ResponseEntity.ok(repository.findByEarlyEnlistmentIdAndUserId(earlyEnlistmentId, userId));
    }

    private void sendEmailAndSms(String authHeader, BigInteger userId, Date appointmentDate, String hospitalName, Date date) throws Exception {
        ApplicationContext context = new AnnotationConfigApplicationContext(ApplicationProperties.class);
        ApplicationProperties properties = context.getBean(ApplicationProperties.class);

        HttpEntity<String> httpRequest = headerToken.tokenHeader(authHeader);

        String userUrl = properties.getUserProfileById() + userId;
        ResponseEntity<UserInfoDto> userResponse = restTemplate.exchange(userUrl, HttpMethod.GET, httpRequest, UserInfoDto.class);
        String emailMessage = "";
        String subject = "";

        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM dd, yyyy");
        String formattedDate = dateFormat.format(date);

        String formattedAppointmentDate = dateFormat.format(appointmentDate);

        subject = "Medical Appointment";
        emailMessage = "Medical Appointment\n" +
                "\n" +
                "Dear " + Objects.requireNonNull(userResponse.getBody()).getFullName() + ",\n" +
                "\n" +
                "In continuation of your early enlistment application submitted on " + formattedDate + ",the Gyalsung Head Office is pleased to inform you that your application has been reviewed. Now you are required to undergo a medical examination. Please report to "+hospitalName+" on "+formattedAppointmentDate+".\n" +
                "\n" +
                "\n" +
                "We look forward to seeing you in the hospital.\n";


        EventBusDto eventBus = EventBusDto.withId(
                Objects.requireNonNull(userResponse.getBody()).getEmail(),
                null,
                null,
                emailMessage,
                subject,
                Objects.requireNonNull(userResponse.getBody()).getMobileNo(),
                null,
                null);

        //todo get data from properties
        addToQueue.addToQueue("email", eventBus);
        addToQueue.addToQueue("sms", eventBus);
    }
}
