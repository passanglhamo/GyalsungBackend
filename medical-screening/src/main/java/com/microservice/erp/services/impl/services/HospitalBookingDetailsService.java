package com.microservice.erp.services.impl.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.microservice.erp.domain.dto.*;
import com.microservice.erp.domain.entities.HospitalBookingDetail;
import com.microservice.erp.domain.entities.MedicalConfiguration;
import com.microservice.erp.domain.repositories.IHospitalBookingDetailsRepository;
import com.microservice.erp.domain.repositories.IMedicalConfigurationRepository;
import com.microservice.erp.services.iServices.IHospitalBookingDetailsService;
import com.microservice.erp.services.impl.AddToQueueMail;
import lombok.AllArgsConstructor;
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

import javax.validation.Valid;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

@Service
@AllArgsConstructor
public class HospitalBookingDetailsService implements IHospitalBookingDetailsService {
    private final AddToQueueMail addToQueue;
    private final IMedicalConfigurationRepository iMedicalConfigurationRepository;
    private final IHospitalBookingDetailsRepository iHospitalBookingDetailsRepository;
    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(5);
    private static final Map<BigInteger, ScheduledFuture<?>> scheduledTasksMap = new HashMap<>();

    @Autowired
    @Qualifier("userProfileTemplate")
    RestTemplate userRestTemplate;

    @Autowired
    @Qualifier("trainingManagementTemplate")
    RestTemplate restTemplate;

    @Override
    public ResponseEntity<?> preBookHospitalAppointment(String authHeader, HospitalBookingDetailsDto hospitalBookingDetailsDto) throws JsonProcessingException {
        BigInteger bookingDetailId;
        HospitalBookingDetail hospitalBookingDetailBooked = iHospitalBookingDetailsRepository.findByUserId(hospitalBookingDetailsDto.getUserId()
        );
        if (!Objects.isNull(hospitalBookingDetailBooked)) {
            bookingDetailId = hospitalBookingDetailBooked.getHospitalBookingDetailId();
            iHospitalBookingDetailsRepository.findById(bookingDetailId).ifPresent(d -> {
                d.setStatus('A');
                d.setUpdatedBy(hospitalBookingDetailsDto.getUserId());
                d.setUpdatedDate(new Date());
                iHospitalBookingDetailsRepository.save(d);
            });
            ScheduledFuture<?> existingTask = scheduledTasksMap.get(bookingDetailId);
            if (existingTask != null) {
                existingTask.cancel(true);
            }

            ScheduledFuture<?> newTask = scheduler.schedule(() -> bookHospitalAppointment(authHeader, BookHospitalCommand.withId(bookingDetailId, null, null, null, null, null, null), 'M'), 2, TimeUnit.MINUTES);
            scheduledTasksMap.put(bookingDetailId, newTask);

        } else {
            HospitalBookingDetail hospitalBookingDetail = new HospitalBookingDetail();
            MedicalConfiguration medicalConfiguration = iMedicalConfigurationRepository.findByHospitalIdAndAppointmentDate(hospitalBookingDetailsDto.getHospitalId(),
                    hospitalBookingDetailsDto.getAppointmentDate()).get();
            HospitalBookingDetail hospitalBookingDetailDb = iHospitalBookingDetailsRepository.findFirstByOrderByHospitalBookingDetailIdDesc();
            BigInteger hospitalBookingDetailId = hospitalBookingDetailDb == null ? BigInteger.ONE : hospitalBookingDetailDb.getHospitalBookingDetailId().add(BigInteger.ONE);
            bookingDetailId = hospitalBookingDetailId;
            hospitalBookingDetail.setHospitalBookingId(medicalConfiguration.getId());
            hospitalBookingDetail.setAmPm(hospitalBookingDetailsDto.getAmPm());
            hospitalBookingDetail.setUserId(hospitalBookingDetailsDto.getUserId());
            hospitalBookingDetail.setHospitalBookingDetailId(hospitalBookingDetailId);
            hospitalBookingDetail.setStatus('A');
            hospitalBookingDetail.setCreatedBy(hospitalBookingDetailsDto.getUserId());
            hospitalBookingDetail.setCreatedDate(new Date());
            iHospitalBookingDetailsRepository.save(hospitalBookingDetail);
            ScheduledFuture<?> newTask = scheduler.schedule(() -> bookHospitalAppointment(authHeader, BookHospitalCommand.withId(bookingDetailId, null, null, null, null, null, null), 'S'), 2, TimeUnit.MINUTES);
            scheduledTasksMap.put(bookingDetailId, newTask);

        }



        return ResponseEntity.ok(bookingDetailId);
    }

    @Override
    public HospitalBookingDetailsDto getHospitalBookingDetailByUserId(String authHeader, BigInteger userId) {
        HospitalBookingDetail hospitalBookingDetail = iHospitalBookingDetailsRepository.findByUserId(userId);
        MedicalConfiguration medicalConfiguration = iMedicalConfigurationRepository.findById(hospitalBookingDetail.getHospitalBookingId()).get();
        ApplicationContext context = new AnnotationConfigApplicationContext(ApplicationProperties.class);
        ApplicationProperties properties = context.getBean(ApplicationProperties.class);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", authHeader);
        HttpEntity<String> request = new HttpEntity<>(headers);
        String hospitalUrl = properties.getTrainingHospitalById() + medicalConfiguration.getHospitalId();
        ResponseEntity<HospitalDto> hospitalDtoResponse = restTemplate.exchange(hospitalUrl, HttpMethod.GET, request, HospitalDto.class);

        return HospitalBookingDetailsDto.withId(
                hospitalBookingDetail.getHospitalBookingDetailId(),
                hospitalBookingDetail.getHospitalBookingId(),
                medicalConfiguration.getHospitalId(),
                medicalConfiguration.getAppointmentDate(),
                hospitalBookingDetail.getAmPm(),
                hospitalBookingDetail.getUserId(),
                Objects.requireNonNull(hospitalDtoResponse.getBody()).getHospitalName()

        );

    }

    @Override
    public ResponseEntity<?> bookHospitalAppointment(String authHeader, @Valid BookHospitalCommand command, Character approveSave) throws JsonProcessingException {
        Optional<HospitalBookingDetail> hospitalBookingDetailDb = iHospitalBookingDetailsRepository.findByHospitalBookingDetailId(command.getHospitalBookingDetailId());
        if (hospitalBookingDetailDb.isPresent()) {
            if (Objects.isNull(command.getStatus())) {
                if (hospitalBookingDetailDb.get().getStatus().equals('A')) {
                    if (approveSave.equals('M')) {
                        iHospitalBookingDetailsRepository.findByHospitalBookingDetailId(command.getHospitalBookingDetailId()).ifPresent(d -> {
                            d.setStatus('B');
                            iHospitalBookingDetailsRepository.save(d);
                        });
                    } else {
                        // Mark the entity for deletion
                        iHospitalBookingDetailsRepository.findByHospitalBookingDetailId(command.getHospitalBookingDetailId()).ifPresent(iHospitalBookingDetailsRepository::delete);
                    }

                }
            } else {
                MedicalConfiguration medicalConfiguration = iMedicalConfigurationRepository.findByHospitalIdAndAppointmentDate(command.getHospitalId(),
                        command.getAppointmentDate()).get();
                iHospitalBookingDetailsRepository.findByHospitalBookingDetailId(command.getHospitalBookingDetailId()).ifPresent(d -> {
                    d.setHospitalBookingId(medicalConfiguration.getId());
                    d.setAmPm(command.getAmPm());
                    d.setUserId(command.getUserId());
                    d.setStatus('B');
                    d.setUpdatedDate(new Date());
                    iHospitalBookingDetailsRepository.save(d);
                });

                ApplicationContext context = new AnnotationConfigApplicationContext(ApplicationProperties.class);
                ApplicationProperties properties = context.getBean(ApplicationProperties.class);
                HttpHeaders headers = new HttpHeaders();
                headers.add("Authorization", authHeader);

                HttpEntity<String> request = new HttpEntity<>(headers);
                String url = properties.getUserProfileById() + command.getUserId();
                ResponseEntity<UserInfoDto> userInfoDtoResponse = userRestTemplate.exchange(url, HttpMethod.GET, request, UserInfoDto.class);
                SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM dd, yyyy");
                String formattedDate = dateFormat.format(command.getAppointmentDate());

                String message = "Dear " + Objects.requireNonNull(userInfoDtoResponse.getBody()).getFullName() + ", " + "You have booked a medical screening appointment for Gyalsung at " + command.getHospitalName() + ", " + "on " + formattedDate + ". "+
                        "Please report "+ (command.getAmPm().equals('Y')?"before":"after") +" 12 p.m. for medical screening. If you want to change the booking date, then please do so 10 days before the appointment date.";
                String subject = "Medical Appointment";

                MailSenderDto eventBus = MailSenderDto.withId(
                        Objects.requireNonNull(userInfoDtoResponse.getBody()).getEmail(),
                        null,
                        null,
                        message,
                        subject,
                        Objects.requireNonNull(userInfoDtoResponse.getBody()).getMobileNo());

                addToQueue.addToQueue("email", eventBus);
                addToQueue.addToQueue("sms", eventBus);

            }
        }


        return ResponseEntity.ok("Booked successfully.");
    }
}
