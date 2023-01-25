package com.microservice.erp.services.impl;

import com.microservice.erp.domain.entities.DefermentInfo;
import com.microservice.erp.domain.entities.EnrolmentInfo;
import com.microservice.erp.domain.entities.ExemptionInfo;
import com.microservice.erp.domain.helper.ApprovalStatus;
import com.microservice.erp.domain.helper.StatusResponse;
import com.microservice.erp.domain.repositories.IDefermentInfoRepository;
import com.microservice.erp.domain.repositories.IEnrolmentInfoRepository;
import com.microservice.erp.domain.repositories.IExemptionInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class DefermentExemptionValidation {
    private final IEnrolmentInfoRepository enrolmentInfoRepository;
    private final IExemptionInfoRepository exemptionInfoRepository;
    private final IDefermentInfoRepository defermentInfoRepository;

    public ResponseEntity<?> getDefermentAndExemptValidation(BigInteger userId) {
        StatusResponse responseMessage = new StatusResponse();
        ExemptionInfo exemptionInfo = exemptionInfoRepository.getExemptionByUserIdNotCancelled(userId, ApprovalStatus.CANCELED.value());
        if (!Objects.isNull(exemptionInfo)) {
            responseMessage.setStatus(ApprovalStatus.APPROVED.value());
            if (exemptionInfo.getStatus().equals(ApprovalStatus.APPROVED.value())) {
                responseMessage.setMessage("There is already one approved exempted application. In order to add new application, please reject the approved exemption.");
                return new ResponseEntity<>(responseMessage, HttpStatus.ALREADY_REPORTED);
            }
            if (exemptionInfo.getStatus().equals(ApprovalStatus.PENDING.value())) {
                responseMessage.setMessage("There is already one pending exempted application. In order to add new application, please reject the pending exemption.");
                return new ResponseEntity<>(responseMessage, HttpStatus.ALREADY_REPORTED);
            }

        }
        DefermentInfo defermentInfo = defermentInfoRepository.getDefermentByUserIdNotCancelled(userId, ApprovalStatus.CANCELED.value());
        if (!Objects.isNull(defermentInfo)) {
            responseMessage.setStatus(ApprovalStatus.APPROVED.value());
            if (defermentInfo.getStatus().equals(ApprovalStatus.PENDING.value())) {
                responseMessage.setMessage("There is already one pending deferred application. In order to add new application, please reject the pending deferment.");
                return new ResponseEntity<>(responseMessage, HttpStatus.ALREADY_REPORTED);
            }
            if (defermentInfo.getStatus().equals(ApprovalStatus.APPROVED.value())) {
                responseMessage.setMessage("There is already one approved deferred application. In order to add new application, please reject the approved deferment.");
                return new ResponseEntity<>(responseMessage, HttpStatus.ALREADY_REPORTED);
            }


        }
        EnrolmentInfo enrolmentInfo = enrolmentInfoRepository.findByUserId(userId);
        if (!Objects.isNull(enrolmentInfo)) {
            responseMessage.setStatus(ApprovalStatus.APPROVED.value());
            if (enrolmentInfo.getStatus().equals(ApprovalStatus.PENDING.value())) {
                responseMessage.setMessage("There is already one pending enrolment application. In order to add new application, please reject the pending enrolment.");
                return new ResponseEntity<>(responseMessage, HttpStatus.ALREADY_REPORTED);
            } else if (enrolmentInfo.getStatus().equals(ApprovalStatus.APPROVED.value())) {
                responseMessage.setMessage("There is already one approved enrolment application. In order to add new application, please reject the approved enrolment.");
                return new ResponseEntity<>(responseMessage, HttpStatus.ALREADY_REPORTED);
            }


        }

        responseMessage.setStatus('I');
        responseMessage.setMessage("No Validation");
        return new ResponseEntity<>(responseMessage, HttpStatus.OK);
    }
}
