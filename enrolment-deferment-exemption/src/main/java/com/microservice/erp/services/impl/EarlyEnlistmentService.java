package com.microservice.erp.services.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.microservice.erp.domain.dto.*;
import com.microservice.erp.domain.entities.EarlyEnlistment;
import com.microservice.erp.domain.entities.GuardianConsent;
import com.microservice.erp.domain.helper.MessageResponse;
import com.microservice.erp.domain.helper.SalutationGenerator;
import com.microservice.erp.domain.repositories.IEarlyEnlistmentRepository;
import com.microservice.erp.domain.repositories.IGuardianConsentRepository;
import com.microservice.erp.services.iServices.IEarlyEnlistmentService;
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
import java.util.Date;
import java.util.Objects;

@Service
public class EarlyEnlistmentService implements IEarlyEnlistmentService {
    @Autowired
    @Qualifier("userProfileTemplate")
    RestTemplate userRestTemplate;

    private final AddToQueue addToQueue;
    private final IGuardianConsentRepository iGuardianConsentRepository;
    private final IEarlyEnlistmentRepository iEarlyEnlistmentRepository;


    public EarlyEnlistmentService(AddToQueue addToQueue, IGuardianConsentRepository iGuardianConsentRepository, IEarlyEnlistmentRepository iEarlyEnlistmentRepository) {
        this.addToQueue = addToQueue;
        this.iGuardianConsentRepository = iGuardianConsentRepository;
        this.iEarlyEnlistmentRepository = iEarlyEnlistmentRepository;
    }

    @Override
    public ResponseEntity<?> requestGuardianConsent(String authTokenHeader, GuardianConsentRequestDto guardianConsentRequestDto) throws JsonProcessingException {

        BigInteger userId = guardianConsentRequestDto.getUserId();
        String domainName = guardianConsentRequestDto.getDomainName();
        ApplicationContext context = new AnnotationConfigApplicationContext(ApplicationProperties.class);
        ApplicationProperties properties = context.getBean(ApplicationProperties.class);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", authTokenHeader);
        HttpEntity<String> request = new HttpEntity<>(headers);

        String userUrl = properties.getUserProfileById() + userId;
        ResponseEntity<UserProfileDto> userResponse = userRestTemplate.exchange(userUrl, HttpMethod.GET, request, UserProfileDto.class);
        String fullName = Objects.requireNonNull(userResponse.getBody()).getFullName().trim();
        Character gender = Objects.requireNonNull(userResponse.getBody()).getGender();
        String guardianName = Objects.requireNonNull(userResponse.getBody()).getGuardianNameFirst().trim();
        String guardianCid = Objects.requireNonNull(userResponse.getBody()).getGuardianCidFirst().trim();
        String guardianMobileNo = Objects.requireNonNull(userResponse.getBody()).getGuardianMobileNoFirst().trim();
        String guardianEmail = Objects.requireNonNull(userResponse.getBody()).getGuardianEmailFirst().trim();
        String relationToGuardian = Objects.requireNonNull(userResponse.getBody()).getRelationToGuardianFirst().trim();

        GuardianConsent guardianConsent = new GuardianConsent();

        GuardianConsent guardianConsentDb = iGuardianConsentRepository.findFirstByOrderByConsentIdDesc();
        BigInteger consentId = guardianConsentDb == null ? BigInteger.ONE : guardianConsentDb.getConsentId().add(BigInteger.ONE);

        guardianConsent.setConsentId(consentId);
        guardianConsent.setUserId(userId);
        guardianConsent.setGuardianName(guardianName);
        guardianConsent.setGuardianCid(guardianCid);
        guardianConsent.setGuardianMobileNo(guardianMobileNo);
        guardianConsent.setGuardianEmail(guardianEmail);
        guardianConsent.setRelationToGuardian(relationToGuardian);
        guardianConsent.setConsentRequestDate(new Date());
        guardianConsent.setStatus('P');//P=Pending, A=Approved, R=Rejected
        guardianConsent.setCreatedBy(userId);
        guardianConsent.setCreatedDate(new Date());
        iGuardianConsentRepository.save(guardianConsent);

        String consentUrl = domainName + "/guardianConsent?consentId=" + consentId;

        String subject = "Request for Parent or Guardian Consent";
        String messageEmail = "Dear " + guardianName + ",<br></br> We would like to request that " +
                SalutationGenerator.getSalutation(gender) + fullName + " has submitted" +
                " a request for your consent regarding " + SalutationGenerator.getPronoun(gender) + " early enlistment in the National Service Training.<br></br><br></br>" +
                "To provide your consent, please <a href='" + consentUrl + "' target='_blank'>Click here</a><br><br><br><br>" +
                "<small>***This is a system-generated email. Please do not respond to this email.***</small>";

        String messageSms = "Dear " + guardianName + ", We would like to request that " +
                SalutationGenerator.getSalutation(gender) + fullName + " has submitted" +
                " a request for your consent regarding " + SalutationGenerator.getPronoun(gender) + " early enlistment in the National Service Training." +
                " To provide your consent, please click here:" + consentUrl;

        EventBus eventBusEmail = EventBus.withId(guardianEmail, null, null, messageEmail, subject, guardianMobileNo, null, null);
        EventBus eventBusSms = EventBus.withId(null, null, null, messageSms, null, guardianMobileNo, null, null);
        addToQueue.addToQueue("email", eventBusEmail);
        addToQueue.addToQueue("sms", eventBusSms);
        return ResponseEntity.ok(new MessageResponse("Guardian consent requested successfully."));
    }

    @Override
    public ResponseEntity<?> getGuardianConsentStatus(BigInteger userId) {
        GuardianConsent guardianConsent = iGuardianConsentRepository.findFirstByUserIdOrderByConsentRequestDateDesc(userId);
        if (guardianConsent != null) {
            return ResponseEntity.ok(guardianConsent);
        } else {
            return ResponseEntity.badRequest().body(new MessageResponse("Data not found."));
        }
    }

    @Override
    public ResponseEntity<?> applyEarlyEnlistment(String authHeader, EarlyEnlistmentDto earlyEnlistmentDto) throws JsonProcessingException {

        ApplicationContext context = new AnnotationConfigApplicationContext(ApplicationProperties.class);
        ApplicationProperties properties = context.getBean(ApplicationProperties.class);

        BigInteger userId = earlyEnlistmentDto.getUserId();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", authHeader);
        HttpEntity<String> request = new HttpEntity<>(headers);

        String userUrl = properties.getUserProfileById() + userId;
        ResponseEntity<UserProfileDto> userResponse = userRestTemplate.exchange(userUrl, HttpMethod.GET, request, UserProfileDto.class);
        String fullName = Objects.requireNonNull(userResponse.getBody()).getFullName().trim();
        String email = Objects.requireNonNull(userResponse.getBody()).getEmail().trim();
        String mobileNo = Objects.requireNonNull(userResponse.getBody()).getMobileNo().trim();
        Character gender = Objects.requireNonNull(userResponse.getBody()).getGender();


        EarlyEnlistment earlyEnlistment = new EarlyEnlistment();

        EarlyEnlistment earlyEnlistmentDb = iEarlyEnlistmentRepository.findFirstByOrderByEnlistmentIdDesc();
        BigInteger enlistmentId = earlyEnlistmentDb == null ? BigInteger.ONE : earlyEnlistmentDb.getEnlistmentId().add(BigInteger.ONE);

        earlyEnlistment.setEnlistmentId(enlistmentId);
        earlyEnlistment.setUserId(userId);

        earlyEnlistment.setApplicationDate(new Date());
        earlyEnlistment.setStatus('P');//P=Pending, A=Approved, R=Rejected
        earlyEnlistment.setCreatedBy(userId);
        earlyEnlistment.setCreatedDate(new Date());
        iEarlyEnlistmentRepository.save(earlyEnlistment);


        String subject = "Early Enlistment";
        String messageEmail = "Dear " + fullName + ",<br></br> You have successfully registered as early enlistment for Training. The Gyalsung " +
                "Head Office will inform you aboutthe medical screeninng and training academy. <br></br><br></br>" +
                "<small>***This is a system-generated email. Please do not respond to this email.***</small>";

        String messageSms = "Dear " + fullName + ", You have successfully registered as early enlistment for Training. The Gyalsung " +
                "Head Office will inform you about the medical screeninng and training academy.";

        EventBus eventBusEmail = EventBus.withId(email, null, null, messageEmail, subject, mobileNo, null, null);
        EventBus eventBusSms = EventBus.withId(null, null, null, messageSms, null, mobileNo, null, null);
        addToQueue.addToQueue("email", eventBusEmail);
        addToQueue.addToQueue("sms", eventBusSms);
        return ResponseEntity.ok(new MessageResponse("Guardian consent requested successfully."));
    }

    @Override
    public ResponseEntity<?> getEarlyEnlistmentStatus(BigInteger userId) {
        EarlyEnlistment earlyEnlistmentDb = iEarlyEnlistmentRepository.findFirstByUserIdOrderByApplicationDateDesc(userId);
        if (earlyEnlistmentDb != null) {
            return ResponseEntity.ok(earlyEnlistmentDb);
        } else {
            return ResponseEntity.badRequest().body(new MessageResponse("Data not found."));
        }
    }
}
