package com.microservice.erp.services.impl.parentConsent;


import com.microservice.erp.domain.dto.ParentConsentListDto;
import com.microservice.erp.domain.dto.parentConsent.ParentConsentDto;
import com.microservice.erp.domain.entities.ParentConsent;
import com.microservice.erp.domain.entities.ParentConsentOtp;
import com.microservice.erp.domain.repositories.ParentConsentOtpRepository;
import com.microservice.erp.domain.repositories.ParentConsentRepository;
import com.microservice.erp.services.helper.MessageResponse;
import com.microservice.erp.services.iServices.parentConsent.IParentConsentService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

@Service
@AllArgsConstructor
public class ParentConsentService implements IParentConsentService {
    private ParentConsentRepository parentConsentRepository;

    private ParentConsentOtpRepository parentConsentOtpRepository;

    @Override
    public ResponseEntity<?> receiveOtp(ParentConsentDto parentConsentDto) {
        ParentConsent parentConsentDb = parentConsentRepository.findByUserId(parentConsentDto.getUserId());
        if (parentConsentDb != null) {
            return ResponseEntity.badRequest().body(new MessageResponse("You have already submitted parent or guardian consent."));
        }

        Random random = new Random();
        int number = random.nextInt(9999);//max upto 9999
        String otp = String.format("%04d", number);

        String message = "Dear " + parentConsentDto.getGuardianName() + ", "
                + parentConsentDto.getFullName() + " has requested parent/guardian consent for Gyalsung Registration." +
                " You have to share this OTP " + otp + " if you agree to send your son/daughter to Gyalsung training." +
                " Please read legal terms and conditions before you share OTP.";
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.exchange("http://172.30.16.213/g2csms/push.php?to=" + parentConsentDto.getGuardianMobileNo() + "&msg=" + message, HttpMethod.GET, null, String.class);
        ParentConsentOtp parentConsentOtp = new ModelMapper().map(parentConsentDto, ParentConsentOtp.class);
        parentConsentOtp.setOtp(otp);
        parentConsentOtpRepository.save(parentConsentOtp);
        //TODO:need to send email to guardian
        return ResponseEntity.ok(parentConsentOtp);
    }

    @Override
    public ResponseEntity<?> submitParentConsent(ParentConsentDto parentConsentDto) {

        ResponseEntity<?> otpVerification = verifyOtp(parentConsentDto);
        if (otpVerification.getStatusCode().value() != HttpStatus.OK.value()) {
            return otpVerification;
        }
        ParentConsent parentConsentDb = parentConsentRepository.findByUserId(parentConsentDto.getUserId());
        if (parentConsentDb != null) {
            return ResponseEntity.badRequest().body(new MessageResponse("You have already submitted parent or guardian consent."));
        }
        ParentConsent parentConsent = new ModelMapper().map(parentConsentDto, ParentConsent.class);
        parentConsentRepository.save(parentConsent);
        //TODO: need to send confirmation sms and email to user
        String smsMessageToUser = "Dear " + parentConsentDto.getFullName() + ", " +
                " Your parent/guardian consent for Gyalsung Registration has been submitted. Congratulations and" +
                " we look forward to seeing you in the training.";
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.exchange("http://172.30.16.213/g2csms/push.php?to=" + parentConsentDto.getMobileNo() + "&msg=" + smsMessageToUser, HttpMethod.GET, null, String.class);

        //TODO: need to send confirmation sms and email to guardian
        String smsMessageToGuardian = "Dear " + parentConsentDto.getGuardianName() + ", " +
                " Thank you for granting consent to " + parentConsentDto.getFullName() + " for Gyalsung Registration." +
                " Congratulations and we look forward to seeing your son/daughter in the training.";
        restTemplate.exchange("http://172.30.16.213/g2csms/push.php?to=" + parentConsentDto.getGuardianMobileNo() + "&msg=" + smsMessageToGuardian, HttpMethod.GET, null, String.class);

        return ResponseEntity.ok(new MessageResponse("Data saved successfully."));
    }

    @Override
    public ResponseEntity<?> getParentConsentList(String authHeader) {
        //TODO: need to filter parent consent lists by year
        List<ParentConsent> parentConsentLists = parentConsentRepository.findAll();

        List<ParentConsentListDto> parentConsentListDtos = new ArrayList<>();
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
