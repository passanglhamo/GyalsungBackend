package com.microservice.erp.services.impl;

import com.microservice.erp.domain.dto.EventBus;
import com.microservice.erp.domain.dto.UserProfileDto;
import com.microservice.erp.domain.entities.DefermentInfo;
import com.microservice.erp.domain.entities.EnrolmentInfo;
import com.microservice.erp.domain.entities.ExemptionInfo;
import com.microservice.erp.domain.helper.ApprovalStatus;
import com.microservice.erp.domain.helper.MessageResponse;
import com.microservice.erp.domain.helper.StatusResponse;
import com.microservice.erp.domain.repositories.IDefermentInfoRepository;
import com.microservice.erp.domain.repositories.IEnrolmentInfoRepository;
import com.microservice.erp.domain.repositories.IExemptionInfoRepository;
import com.microservice.erp.services.iServices.IUpdateExemptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigInteger;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UpdateExemptionService implements IUpdateExemptionService {

    private final IExemptionInfoRepository repository;
    private final IDefermentInfoRepository defermentInfoRepository;
    private final IEnrolmentInfoRepository enrolmentInfoRepository;
    private final HeaderToken headerToken;
    private final AddToQueue addToQueue;
    private final DefermentExemptionValidation defermentExemptionValidation;


    @Override
    public ResponseEntity<?> approveByIds(String authHeader, UpdateExemptionCommand command) {

        ExemptionInfo exemptionInfo = repository.findAllById(command.getExemptionIds())
                .stream()
                .filter(d -> (d.getStatus().equals(ApprovalStatus.APPROVED.value()) ||
                        d.getStatus().equals(ApprovalStatus.REJECTED.value()))
                ).findFirst().orElse(null);

        if (!Objects.isNull(exemptionInfo)) {
            return new ResponseEntity<>("There are some application that are already approved or rejected.", HttpStatus.ALREADY_REPORTED);

        }

        repository.findAllById(command.getExemptionIds()).forEach(d -> {
//            StatusResponse responseMessage = (StatusResponse) defermentExemptionValidation
//                    .getDefermentAndExemptValidation(d.getUserId(), 'E').getBody();
//            if (!Objects.isNull(responseMessage)) {
//
//                if (responseMessage.getSavingStatus().equals("DP") ||
//                        responseMessage.getSavingStatus().equals("DA")) {
//                    DefermentInfo defermentInfo = defermentInfoRepository.getDefermentByUserId(d.getUserId());
//                    defermentInfoRepository.findById(defermentInfo.getId()).ifPresent(dval -> {
//                        dval.setStatus(ApprovalStatus.CANCELED.value());
//                        defermentInfoRepository.save(dval);
//                    });
//                }
//                if (responseMessage.getSavingStatus().equals("EP")) {
//                    ExemptionInfo exemptionInfoVal = repository.getExemptionByUserId(d.getUserId());
//                    if (exemptionInfoVal.getStatus().equals(ApprovalStatus.PENDING.value())) {
//                        repository.findById(exemptionInfoVal.getId()).ifPresent(eVal -> {
//                            eVal.setStatus(ApprovalStatus.CANCELED.value());
//                            repository.save(eVal);
//                        });
//                    }
//                }
//                if (responseMessage.getSavingStatus().equals("ENP")||
//                        responseMessage.getSavingStatus().equals("ENA")) {
//                    ExemptionInfo exemptionInfoVal = repository.getExemptionByUserId(d.getUserId());
//                    if (exemptionInfoVal.getStatus().equals(ApprovalStatus.PENDING.value())) {
//                        repository.findById(exemptionInfoVal.getId()).ifPresent(dVal -> {
//                            dVal.setStatus(ApprovalStatus.CANCELED.value());
//                            repository.save(dVal);
//                        });
//                    }
//                    DefermentInfo defermentInfoVal = defermentInfoRepository.getDefermentByUserId(d.getUserId());
//                    if (exemptionInfoVal.getStatus().equals(ApprovalStatus.PENDING.value())
//                            || exemptionInfoVal.getStatus().equals(ApprovalStatus.APPROVED.value())) {
//                        repository.findById(defermentInfoVal.getId()).ifPresent(dVal -> {
//                            dVal.setStatus(ApprovalStatus.CANCELED.value());
//                            repository.save(dVal);
//                        });
//                    }
//
//                    EnrolmentInfo enrolmentInfo = enrolmentInfoRepository.findByUserId(d.getUserId());
//                    if (exemptionInfoVal.getStatus().equals(ApprovalStatus.PENDING.value())
//                            || exemptionInfoVal.getStatus().equals(ApprovalStatus.APPROVED.value())) {
//                        enrolmentInfoRepository.findById(enrolmentInfo.getId()).ifPresent(dVal -> {
//                            dVal.setStatus(ApprovalStatus.CANCELED.value());
//                            enrolmentInfoRepository.save(dVal);
//                        });
//                    }
//
//                }
//            }
            if (d.getStatus().equals(ApprovalStatus.PENDING.value())) {
                d.setStatus(ApprovalStatus.APPROVED.value());
                d.setApprovalRemarks(command.getRemarks());
                repository.save(d);
            }

            try {
                sendEmailAndSms(authHeader, d.getUserId(), ApprovalStatus.APPROVED.value());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        return ResponseEntity.ok(new MessageResponse("Approved successfully"));
    }

    @Override
    public ResponseEntity<?> rejectByIds(String authHeader, UpdateExemptionCommand command) {

        ExemptionInfo exemptionInfo = repository.findAllById(command.getExemptionIds())
                .stream()
                .filter(d -> (d.getStatus().equals(ApprovalStatus.APPROVED.value()) ||
                        d.getStatus().equals(ApprovalStatus.REJECTED.value()))
                ).findFirst().orElse(null);

        if (!Objects.isNull(exemptionInfo)) {
            return new ResponseEntity<>("There are some application that are already approved or rejected.", HttpStatus.ALREADY_REPORTED);

        }

        repository.findAllById(command.getExemptionIds()).forEach(d -> {
            if(d.getStatus().equals(ApprovalStatus.PENDING.value())){
                d.setStatus(ApprovalStatus.REJECTED.value());
                d.setApprovalRemarks(command.getRemarks());
                repository.save(d);
            }
            try {
                sendEmailAndSms(authHeader, d.getUserId(), ApprovalStatus.REJECTED.value());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        });

        return ResponseEntity.ok(new MessageResponse("Rejected successfully"));

    }

    private void sendEmailAndSms(String authHeader, BigInteger userId, Character status) throws Exception {
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> httpRequest = headerToken.tokenHeader(authHeader);

        String userUrl = "http://localhost:81/api/user/profile/userProfile/getProfileInfo?userId=" + userId;
        ResponseEntity<UserProfileDto> userResponse = restTemplate.exchange(userUrl, HttpMethod.GET, httpRequest, UserProfileDto.class);

        String emailMessage = "";
        String subject = "";

        if (status.equals(ApprovalStatus.APPROVED.value())) {
            subject = "Approved for Exemption";
            emailMessage = "Deferment from Gyalsung Training\n" +
                    "\n" +
                    "Dear " + Objects.requireNonNull(userResponse.getBody()).getFullName() + ",\n" +
                    "\n" +
                    "We are pleased to inform you that you have been granted exemption from Gyalsung training. If the circumstance necessitates you for further exemption, please contact 00000 or visit Gyalsung HQ with all your supporting documents. \n" +
                    "\n" +
                    " Warm Regards, \n" +
                    "\n" +
                    " Gyalsung HQ \n";
        } else {
            subject = "Rejection for Exemption";
            emailMessage = "Dear " + Objects.requireNonNull(userResponse.getBody()).getFullName() + ",\n" +
                    "\n" +
                    "Thank you for your recent application. We regret to inform you that your exemption has not been approved by the reviewing committee. For further clarification please contact us at 00000 or visit us at Gyalsung HQ. Since your deferment is not approved, you are liable for your Gyalsung Training. Please complete all the pre-listment procedure and be ready for your Gyalsung training.  \n" +
                    "\n" +
                    "Please visit www.gyalsung.org.bt  for details on registration, enlistment, offences etc. \n" +
                    "\n" +
                    "We wish you all the best for your registration. \n" +
                    "Warm Regards,\n" +
                    "Gyalsung HQ.\n";
        }

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
