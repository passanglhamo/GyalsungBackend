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
            if (exemptionInfo.getStatus().equals(ApprovalStatus.APPROVED.value())) {
                responseMessage.setStatus(ApprovalStatus.APPROVED.value());
                responseMessage.setSavingStatus("EA");
                responseMessage.setMessage("There is approved exemption. You can not proceed.");
                return new ResponseEntity<>(responseMessage, HttpStatus.ALREADY_REPORTED);
            }
            if (exemptionInfo.getStatus().equals(ApprovalStatus.PENDING.value())) {
                responseMessage.setStatus(ApprovalStatus.PENDING.value());
                responseMessage.setSavingStatus("EP");
                responseMessage.setMessage("There is still some exemption which are not approved. If you continue," +
                        " then the pending exemption will be cancelled.");
                return new ResponseEntity<>(responseMessage, HttpStatus.ALREADY_REPORTED);

            }

        }
        DefermentInfo defermentInfo = defermentInfoRepository.getDefermentByUserIdNotCancelled(userId, ApprovalStatus.CANCELED.value());
        if (!Objects.isNull(defermentInfo)) {
            if (defermentInfo.getStatus().equals(ApprovalStatus.PENDING.value())) {
                responseMessage.setStatus(ApprovalStatus.PENDING.value());
                responseMessage.setSavingStatus("DP");
                responseMessage.setMessage("There is still some deferment which are not approved. If you continue," +
                        " then the pending deferment will be cancelled.");
                return new ResponseEntity<>(responseMessage, HttpStatus.ALREADY_REPORTED);
            }
            if (defermentInfo.getStatus().equals(ApprovalStatus.APPROVED.value())) {
                responseMessage.setStatus(ApprovalStatus.PENDING.value());
                responseMessage.setSavingStatus("DA");
                responseMessage.setMessage("There is approved deferment. If you continue, then deferment application will be cancelled.");
                return new ResponseEntity<>(responseMessage, HttpStatus.ALREADY_REPORTED);
            }

        }
        EnrolmentInfo enrolmentInfo = enrolmentInfoRepository.findByUserId(userId);
        if (!Objects.isNull(enrolmentInfo)) {
            if (enrolmentInfo.getStatus().equals(ApprovalStatus.PENDING.value())) {
                responseMessage.setStatus(ApprovalStatus.PENDING.value());
                responseMessage.setSavingStatus("ENP");
                responseMessage.setMessage("There is still some enrolment which are not approved. If you continue," +
                        " then the pending enrolment will be cancelled.");
                return new ResponseEntity<>(responseMessage, HttpStatus.ALREADY_REPORTED);
            } else if (enrolmentInfo.getStatus().equals(ApprovalStatus.APPROVED.value())) {
                responseMessage.setStatus(ApprovalStatus.PENDING.value());
                responseMessage.setSavingStatus("ENA");
                responseMessage.setMessage("There is approved enrolment. If you continue, then enrolment application will be cancelled.");
                return new ResponseEntity<>(responseMessage, HttpStatus.ALREADY_REPORTED);

            }
        }

        responseMessage.setStatus('I');
        responseMessage.setSavingStatus(ApprovalStatus.APPROVED.value().toString());
        responseMessage.setMessage("No Validation");
        return new ResponseEntity<>(responseMessage, HttpStatus.OK);
    }
}
