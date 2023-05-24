package com.microservice.erp.services.impl;


import com.microservice.erp.domain.dao.ParentConsentDao;
import com.microservice.erp.domain.dto.ApplicationProperties;
import com.microservice.erp.domain.dto.EventBus;
import com.microservice.erp.domain.dto.ParentConsentDto;
import com.microservice.erp.domain.dto.ParentConsentListDto;
import com.microservice.erp.domain.entities.ParentConsent;
import com.microservice.erp.domain.entities.ParentConsentOtp;
import com.microservice.erp.domain.entities.RegistrationDateInfo;
import com.microservice.erp.domain.helper.MessageResponse;
import com.microservice.erp.domain.repositories.IRegistrationDateInfoRepository;
import com.microservice.erp.domain.repositories.ParentConsentOtpRepository;
import com.microservice.erp.domain.repositories.ParentConsentRepository;
import com.microservice.erp.services.iServices.IParentConsentService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
@AllArgsConstructor
public class ParentConsentService implements IParentConsentService {
    private ParentConsentRepository parentConsentRepository;
    private ParentConsentDao parentConsentDao;
    private ParentConsentOtpRepository parentConsentOtpRepository;
    private IRegistrationDateInfoRepository iRegistrationDateInfoRepository;
    private AddToQueue addToQueue;

    @Autowired
    @Qualifier("userProfileTemplate")
    RestTemplate restTemplate;


    @Override
    public ResponseEntity<?> receiveOtp(ParentConsentDto parentConsentDto) throws Exception {
        ParentConsent parentConsentDb = parentConsentRepository.findByUserId(parentConsentDto.getUserId());
        if (parentConsentDb != null) {
            return ResponseEntity.badRequest().body(new MessageResponse("You have already submitted parent or guardian consent."));
        }

        Random random = new Random();
        int number = random.nextInt(9999);//max upto 9999
        String otp = String.format("%04d", number);

        String message = "Dear " + parentConsentDto.getGuardianName() + ", " + parentConsentDto.getFullName() + " has requested parent/guardian consent for Gyalsung Registration." + " You have to share this OTP " + otp + " if you agree to send your son/daughter to Gyalsung training." + " Please read legal terms and conditions before you share OTP.";

        EventBus eventBus = EventBus.withId(null, null, null, message, null, parentConsentDto.getGuardianMobileNo());
        addToQueue.addToQueue("sms", eventBus);

        ParentConsentOtp parentConsentOtp = new ModelMapper().map(parentConsentDto, ParentConsentOtp.class);
        parentConsentOtp.setOtp(otp);
        parentConsentOtpRepository.save(parentConsentOtp);

        String subject = "Parent/Guardian Consent for Gyalsung Registration";

        EventBus eventBusMail = EventBus.withId(parentConsentDto.getGuardianEmail(), null, null, message, subject, null);

        //todo need to get from properties
        addToQueue.addToQueue("email", eventBusMail);

        return ResponseEntity.ok(parentConsentOtp);
    }

    @Override
    public ResponseEntity<?> submitParentConsent(ParentConsentDto parentConsentDto) throws Exception {

        ResponseEntity<?> otpVerification = verifyOtp(parentConsentDto);
        if (otpVerification.getStatusCode().value() != HttpStatus.OK.value()) {
            return otpVerification;
        }
        ParentConsent parentConsentDb = parentConsentRepository.findByUserId(parentConsentDto.getUserId());
        if (parentConsentDb != null) {
            return ResponseEntity.badRequest().body(new MessageResponse("You have already submitted parent or guardian consent."));
        }
        ParentConsent parentConsent = new ModelMapper().map(parentConsentDto, ParentConsent.class);

        //todo remove static code
        RegistrationDateInfo registrationDateInfo = iRegistrationDateInfoRepository.findByStatus('A');
        parentConsent.setYear(registrationDateInfo.getRegistrationYear());
        parentConsent.setSubmittedOn(new Date());
        parentConsentRepository.save(parentConsent);
        // to send confirmation sms and email to user
        String messageToUser = "Dear " + parentConsentDto.getFullName() + ", " + " Your parent/guardian consent for Gyalsung Registration has been submitted. Congratulations and" + " we look forward to seeing you in the training.";
        String subject = "Parent/Guardian Consent for Gyalsung Registration";

        EventBus eventBusMail= EventBus.withId(
                parentConsentDto.getEmail(),
                null,
                null,
                messageToUser,
                subject,
                parentConsentDto.getMobileNo());

        //todo need to get from properties
        addToQueue.addToQueue("email",eventBusMail);
        addToQueue.addToQueue("sms",eventBusMail);

        //to send confirmation sms and email to guardian
        String messageToGuardian = "Dear " + parentConsentDto.getGuardianName() + ", " + " Thank you for granting consent to " + parentConsentDto.getFullName() + " for Gyalsung Registration." + " Congratulations and we look forward to seeing your son/daughter in the training.";

        EventBus mailSenderGuardianDto= EventBus.withId(
                parentConsentDto.getGuardianEmail(),
                null,
                null,
                messageToGuardian,
                subject,
                parentConsentDto.getGuardianMobileNo());

//todo need to get from properties
        addToQueue.addToQueue("email",mailSenderGuardianDto);
        addToQueue.addToQueue("sms",mailSenderGuardianDto);
        return ResponseEntity.ok(new MessageResponse("Data saved successfully."));
    }

    @Override
    public ResponseEntity<?> getParentConsentList(String authHeader, String year, Character status) {
        ApplicationContext context = new AnnotationConfigApplicationContext(ApplicationProperties.class);
        ApplicationProperties properties = context.getBean(ApplicationProperties.class);

        List<ParentConsentListDto> parentConsentListDtos = new ArrayList<>();
        //todo remove static code
        if (status == 'S') {
            List<ParentConsent> parentConsentLists = parentConsentRepository.findByYearOrderBySubmittedOnAsc(year);
            parentConsentLists.forEach(item -> {
                HttpHeaders headers = new HttpHeaders();
                headers.add("Authorization", authHeader);
                HttpEntity<String> request = new HttpEntity<>(headers);
                String url = properties.getUserProfileById() + item.getUserId();
                ResponseEntity<ParentConsentListDto> response = restTemplate.exchange(url, HttpMethod.GET, request, ParentConsentListDto.class);
                ParentConsentListDto parentConsentListDto = new ParentConsentListDto();
                parentConsentListDto.setUserId(item.getUserId());
                parentConsentListDto.setFullName(Objects.requireNonNull(response.getBody()).getFullName());
                parentConsentListDto.setCid(response.getBody().getCid());
                parentConsentListDto.setDob(response.getBody().getDob());
                parentConsentListDto.setGuardianName(item.getGuardianName());
                parentConsentListDto.setGuardianMobileNo(item.getGuardianMobileNo());
                parentConsentListDto.setSubmittedOn(item.getSubmittedOn());
                parentConsentListDtos.add(parentConsentListDto);
            });
        } else {
            List<ParentConsentDto> eligibleParentConsentList = parentConsentDao.getEligibleParentConsentList(year);
            if(Objects.isNull(eligibleParentConsentList)){
                return ResponseEntity.badRequest().body(new MessageResponse("There is no eligible parent consent."));
            }
            
            eligibleParentConsentList.forEach(item -> {
                HttpHeaders headers = new HttpHeaders();
                headers.add("Authorization", authHeader);
                HttpEntity<String> request = new HttpEntity<>(headers);
                String url = properties.getUserProfileById() + item.getUserId();
                ResponseEntity<ParentConsentListDto> response = restTemplate.exchange(url, HttpMethod.GET, request, ParentConsentListDto.class);
                ParentConsentListDto parentConsentListDto = new ParentConsentListDto();
                parentConsentListDto.setUserId(item.getUserId());
                parentConsentListDto.setFullName(Objects.requireNonNull(response.getBody()).getFullName());
                parentConsentListDto.setCid(response.getBody().getCid());
                parentConsentListDto.setDob(response.getBody().getDob());
                parentConsentListDto.setGuardianName(response.getBody().getGuardianName());
                parentConsentListDto.setGuardianMobileNo(response.getBody().getGuardianMobileNo());
                parentConsentListDtos.add(parentConsentListDto);
            });
        }

        return ResponseEntity.ok(parentConsentListDtos);
    }

    private ResponseEntity<?> verifyOtp(ParentConsentDto parentConsentDto) {
        ParentConsentOtp parentConsentOtp = parentConsentOtpRepository.findByUserId(parentConsentDto.getUserId());
        if (parentConsentOtp == null) {
            return ResponseEntity.badRequest().body(new MessageResponse("OTP didn't found."));
        }
        if (!Objects.equals(parentConsentOtp.getOtp(), parentConsentDto.getOtp())) {
            return ResponseEntity.badRequest().body(new MessageResponse("OTP didn't matched."));
        } else {
            return ResponseEntity.ok("");
        }
    }
}
