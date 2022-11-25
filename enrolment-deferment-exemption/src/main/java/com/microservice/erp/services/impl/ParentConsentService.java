package com.microservice.erp.services.impl;


import com.microservice.erp.domain.dao.ParentConsentDao;
import com.microservice.erp.domain.dto.ParentConsentListDto;
import com.microservice.erp.domain.dto.ParentConsentDto;
import com.microservice.erp.domain.entities.ParentConsent;
import com.microservice.erp.domain.entities.ParentConsentOtp;
import com.microservice.erp.domain.entities.RegistrationDateInfo;
import com.microservice.erp.domain.repositories.IRegistrationDateInfoRepository;
import com.microservice.erp.domain.repositories.ParentConsentOtpRepository;
import com.microservice.erp.domain.repositories.ParentConsentRepository;
import com.microservice.erp.domain.helper.MailSender;
import com.microservice.erp.domain.helper.MessageResponse;
import com.microservice.erp.domain.helper.SmsSender;
import com.microservice.erp.services.iServices.IParentConsentService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
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
        SmsSender.sendSms(parentConsentDto.getGuardianMobileNo(), message);

        ParentConsentOtp parentConsentOtp = new ModelMapper().map(parentConsentDto, ParentConsentOtp.class);
        parentConsentOtp.setOtp(otp);
        parentConsentOtpRepository.save(parentConsentOtp);

        String subject = "Parent/Guardian Consent for Gyalsung Registration";
        MailSender.sendMail(parentConsentDto.getGuardianEmail(), null, null, message, subject);
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

        RegistrationDateInfo registrationDateInfo = iRegistrationDateInfoRepository.findByStatus('A');
        parentConsent.setYear(registrationDateInfo.getRegistrationYear());
        parentConsent.setSubmittedOn(new Date());
        parentConsentRepository.save(parentConsent);
        // to send confirmation sms and email to user
        String messageToUser = "Dear " + parentConsentDto.getFullName() + ", " + " Your parent/guardian consent for Gyalsung Registration has been submitted. Congratulations and" + " we look forward to seeing you in the training.";
        SmsSender.sendSms(parentConsentDto.getMobileNo(), messageToUser);

        String subject = "Parent/Guardian Consent for Gyalsung Registration";
        MailSender.sendMail(parentConsentDto.getEmail(), null, null, messageToUser, subject);

        //to send confirmation sms and email to guardian
        String messageToGuardian = "Dear " + parentConsentDto.getGuardianName() + ", " + " Thank you for granting consent to " + parentConsentDto.getFullName() + " for Gyalsung Registration." + " Congratulations and we look forward to seeing your son/daughter in the training.";
        SmsSender.sendSms(parentConsentDto.getGuardianMobileNo(), messageToGuardian);
        MailSender.sendMail(parentConsentDto.getGuardianEmail(), null, null, messageToGuardian, subject);
        return ResponseEntity.ok(new MessageResponse("Data saved successfully."));
    }

    @Override
    public ResponseEntity<?> getParentConsentList(String authHeader, String year, Character status) {


        List<ParentConsentListDto> parentConsentListDtos = new ArrayList<>();
        if (status == 'S') {
            List<ParentConsent> parentConsentLists = parentConsentRepository.findByYearOrderBySubmittedOnAsc(year);
            parentConsentLists.forEach(item -> {
                RestTemplate restTemplate = new RestTemplate();
                HttpHeaders headers = new HttpHeaders();
                headers.add("Authorization", authHeader);
                HttpEntity<String> request = new HttpEntity<>(headers);
                String url = "http://localhost:81/api/user/profile/userProfile/getProfileInfo?userId=" + item.getUserId();
                ResponseEntity<ParentConsentListDto> response = restTemplate.exchange(url, HttpMethod.GET, request, ParentConsentListDto.class);
                ParentConsentListDto parentConsentListDto = new ParentConsentListDto();
                parentConsentListDto.setUserId(item.getUserId());
                parentConsentListDto.setFullName(Objects.requireNonNull(response.getBody()).getFullName());
                parentConsentListDto.setCid(response.getBody().getCid());
                parentConsentListDto.setDob(response.getBody().getDob());
                parentConsentListDto.setGuardianName(item.getGuardianName());
                parentConsentListDto.setGuardianMobileNo(item.getGuardianMobileNo());
                parentConsentListDtos.add(parentConsentListDto);
            });
        } else {
            List<ParentConsentDto> eligibleParentConsentList = parentConsentDao.getEligibleParentConsentList(year);
            eligibleParentConsentList.forEach(item -> {
                RestTemplate restTemplate = new RestTemplate();
                HttpHeaders headers = new HttpHeaders();
                headers.add("Authorization", authHeader);
                HttpEntity<String> request = new HttpEntity<>(headers);
                String url = "http://localhost:81/api/user/profile/userProfile/getProfileInfo?userId=" + item.getUser_id();
                ResponseEntity<ParentConsentListDto> response = restTemplate.exchange(url, HttpMethod.GET, request, ParentConsentListDto.class);
                ParentConsentListDto parentConsentListDto = new ParentConsentListDto();
                parentConsentListDto.setUserId(item.getUser_id());
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
