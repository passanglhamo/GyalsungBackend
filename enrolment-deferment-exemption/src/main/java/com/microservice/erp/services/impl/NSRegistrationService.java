package com.microservice.erp.services.impl;

import com.microservice.erp.domain.dto.ApplicationProperties;
import com.microservice.erp.domain.dto.EventBus;
import com.microservice.erp.domain.dto.NSRegistrationDto;
import com.microservice.erp.domain.dto.UserProfileDto;
import com.microservice.erp.domain.entities.NSRegistration;
import com.microservice.erp.domain.entities.RegistrationDateInfo;
import com.microservice.erp.domain.helper.ActiveStatus;
import com.microservice.erp.domain.helper.MessageResponse;
import com.microservice.erp.domain.mapper.NSRegistrationMapper;
import com.microservice.erp.domain.repositories.INSRegistrationRepository;
import com.microservice.erp.domain.repositories.IRegistrationDateInfoRepository;
import com.microservice.erp.services.iServices.INSRegistrationService;
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

import java.math.BigInteger;
import java.util.Objects;

@Service
@AllArgsConstructor
public class NSRegistrationService implements INSRegistrationService {

    private final IRegistrationDateInfoRepository iRegistrationDateInfoRepository;
    private final INSRegistrationRepository repository;
    private final AddToQueue addToQueue;
    private final NSRegistrationMapper mapper;


    @Autowired
    @Qualifier("userProfileTemplate")
    RestTemplate userRestTemplate;

    @Override
    public ResponseEntity<?> save(String authHeader, NSRegistrationDto nsRegistrationDto) throws Exception {
        ApplicationContext context = new AnnotationConfigApplicationContext(ApplicationProperties.class);
        ApplicationProperties properties = context.getBean(ApplicationProperties.class);

        RegistrationDateInfo registrationDateInfo = iRegistrationDateInfoRepository.findByStatus(ActiveStatus.Active.value());
        if (registrationDateInfo == null) {
            return ResponseEntity.badRequest().body(new MessageResponse("Registration date information not found."));
        }
        String registrationYear = registrationDateInfo.getRegistrationYear();

        NSRegistration alreadyExistNsRegis = repository.findByUserId(new BigInteger(String.valueOf(nsRegistrationDto.getUserId())));
        //to check already registered or not
        if (alreadyExistNsRegis != null) {
            return ResponseEntity.badRequest().body(new MessageResponse("You have already registered."));
        }
        nsRegistrationDto.setYear(registrationYear);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", authHeader);
        HttpEntity<String> request = new HttpEntity<>(headers);

        String url = properties.getUserProfileById() + nsRegistrationDto.getUserId();
        ResponseEntity<UserProfileDto> userDtoResponse = userRestTemplate.exchange(url, HttpMethod.GET, request, UserProfileDto.class);

        Character gender = Objects.requireNonNull(userDtoResponse.getBody()).getGender();
        String fullName = userDtoResponse.getBody().getFullName();
        String mobileNo = userDtoResponse.getBody().getMobileNo();
        String email = userDtoResponse.getBody().getEmail();
        nsRegistrationDto.setGender(gender);

        var nsRegistration = repository.save(
                mapper.mapToEntity(nsRegistrationDto)
        );

        repository.save(nsRegistration);


        String message = "Dear " + fullName + ",  Thank you for registering to Gyalsung training.";
        String subject = "Gyalsung Registration";

        EventBus eventBus = EventBus.withId(email, null, null, message, subject, mobileNo, null, null);

        //todo get from properties
        addToQueue.addToQueue("email", eventBus);
        addToQueue.addToQueue("sms", eventBus);

        return ResponseEntity.ok(new MessageResponse("Registered successfully."));
    }

    @Override
    public ResponseEntity<?> getMyRegistrationInfo(BigInteger userId) {
        NSRegistration nsRegistration = repository.findByUserId(userId);
        if (nsRegistration != null) {
            //enrolmentInfo.setEnrolmentCoursePreferences(null);
            return ResponseEntity.ok(nsRegistration);
        } else {
            return ResponseEntity.badRequest().body("Information not found.");
        }
    }
}
