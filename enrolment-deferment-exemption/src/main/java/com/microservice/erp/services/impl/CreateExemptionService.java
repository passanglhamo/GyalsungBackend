package com.microservice.erp.services.impl;

import com.microservice.erp.domain.dto.EventBus;
import com.microservice.erp.domain.dto.UserProfileDto;
import com.microservice.erp.domain.entities.DefermentInfo;
import com.microservice.erp.domain.entities.EnrolmentInfo;
import com.microservice.erp.domain.entities.ExemptionInfo;
import com.microservice.erp.domain.helper.ApprovalStatus;
import com.microservice.erp.domain.helper.MessageResponse;
import com.microservice.erp.domain.helper.StatusResponse;
import com.microservice.erp.domain.mapper.ExemptionMapper;
import com.microservice.erp.domain.repositories.IDefermentInfoRepository;
import com.microservice.erp.domain.repositories.IEnrolmentInfoRepository;
import com.microservice.erp.domain.repositories.IExemptionInfoRepository;
import com.microservice.erp.services.iServices.ICreateExemptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.math.BigInteger;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CreateExemptionService implements ICreateExemptionService {
    private final IExemptionInfoRepository repository;
    private final IDefermentInfoRepository defermentInfoRepository;
    private final IEnrolmentInfoRepository enrolmentInfoRepository;
    private final ExemptionMapper mapper;
    private final AddToQueue addToQueue;
    private final HeaderToken headerToken;
    private final DefermentExemptionValidation defermentExemptionValidation;
    Integer fileLength = 5;


    @Transactional(rollbackOn = Exception.class)
    public ResponseEntity<?> saveExemption(HttpServletRequest request, CreateExemptionCommand command) throws Exception {
        String authTokenHeader = request.getHeader("Authorization");
        if (!Objects.isNull(command.getProofDocuments())) {
            if (command.getProofDocuments().length > fileLength) {
                return new ResponseEntity<>("You can upload maximum of 5 files.", HttpStatus.ALREADY_REPORTED);
            }
        }
        StatusResponse responseMessage = (StatusResponse) defermentExemptionValidation
                .getDefermentAndExemptValidation(command.getUserId()).getBody();
        if (!Objects.isNull(responseMessage)) {
            if (responseMessage.getSavingStatus().equals("EA")) {
                return new ResponseEntity<>("User is exempted from the gyalsung program.", HttpStatus.ALREADY_REPORTED);

            }

            if (responseMessage.getSavingStatus().equals("DP")
                    || responseMessage.getSavingStatus().equals("DA")) {
                DefermentInfo defermentInfoVal = defermentInfoRepository.getDefermentByUserId(command.getUserId());
                repository.findById(defermentInfoVal.getId()).ifPresent(dVal -> {
                    dVal.setStatus(ApprovalStatus.CANCELED.value());
                    repository.save(dVal);
                });
            }
            if (responseMessage.getSavingStatus().equals("EP")) {
                ExemptionInfo exemptionInfo = repository.getExemptionByUserId(command.getUserId());
                if (exemptionInfo.getStatus().equals(ApprovalStatus.PENDING.value())) {
                    repository.findById(exemptionInfo.getId()).ifPresent(dVal -> {
                        dVal.setStatus(ApprovalStatus.CANCELED.value());
                        repository.save(dVal);
                    });
                }
            }
            if (responseMessage.getSavingStatus().equals("ENP") || responseMessage.getSavingStatus().equals("ENA")) {
                EnrolmentInfo enrolmentInfo = enrolmentInfoRepository.findByUserId(command.getUserId());
                if (enrolmentInfo.getStatus().equals(ApprovalStatus.PENDING.value())) {
                    enrolmentInfoRepository.findById(enrolmentInfo.getId()).ifPresent(dVal -> {
                        dVal.setStatus(ApprovalStatus.CANCELED.value());
                        enrolmentInfoRepository.save(dVal);
                    });
                }
            }
        }


        var exemption = repository.save(
                mapper.mapToEntity(
                        request, command
                )
        );

        repository.save(exemption);

        sendEmailAndSms(authTokenHeader, exemption.getUserId());

        return ResponseEntity.ok(new MessageResponse("An acknowledgement notifcation will be sent  to you as soon as you submit your  application.\" +\n" +
                "                    \"Your Exemption application will be  reviewed and the outcome  of the exemption wil be sent \" +\n" +
                "                    \" to you throught your email. If you  are not approved for exemption , you will have to complete the \" +\n" +
                "                    \" Gyalsung pre-enlistment procedure"));
    }

    private void sendEmailAndSms(String authTokenHeader, BigInteger userId) throws Exception {
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> httpRequest = headerToken.tokenHeader(authTokenHeader);

        String userUrl = "http://localhost:81/api/user/profile/userProfile/getProfileInfo?userId=" + userId;
        ResponseEntity<UserProfileDto> userResponse = restTemplate.exchange(userUrl, HttpMethod.GET, httpRequest, UserProfileDto.class);

        String subject = "Acknowledged for Exemption";

        String emailMessage = "Dear " + Objects.requireNonNull(userResponse.getBody()).getFullName() + ",\n" +
                "\n" +
                "This is to acknowledge the receipt of your exemption application. Your exemption application will be reviewed and the outcome of the exemption will be sent to you through your email within 10 days of the submission of your application. If you are not approved for exemption, you will have to complete the Gyalsung pre-enlistment procedure. \n";
        EventBus eventBus = EventBus.withId(
                Objects.requireNonNull(userResponse.getBody()).getEmail(),
                null,
                null,
                emailMessage,
                subject,
                Objects.requireNonNull(userResponse.getBody()).getMobileNo());

        addToQueue.addToQueue("email", eventBus);
        addToQueue.addToQueue("sms", eventBus);
    }
}
