package com.microservice.erp.services.impl;

import com.microservice.erp.domain.dto.ApplicationProperties;
import com.microservice.erp.domain.dto.EventBus;
import com.microservice.erp.domain.dto.UserProfileDto;
import com.microservice.erp.domain.helper.ApprovalStatus;
import com.microservice.erp.domain.helper.MailSentStatus;
import com.microservice.erp.domain.helper.MessageResponse;
import com.microservice.erp.domain.helper.RoleStatus;
import com.microservice.erp.domain.mapper.DefermentMapper;
import com.microservice.erp.domain.repositories.IDefermentInfoAuditRepository;
import com.microservice.erp.domain.repositories.IDefermentInfoRepository;
import com.microservice.erp.services.iServices.IUpdateDefermentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
import java.math.BigInteger;
import java.util.Date;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UpdateDefermentService implements IUpdateDefermentService {
    private final IDefermentInfoRepository repository;
    private final HeaderToken headerToken;
    private final AddToQueue addToQueue;
    private final MailToOperator mailToOperator;
    private final IDefermentInfoAuditRepository auditRepository;
    private final DefermentMapper mapper;

    @Autowired
    @Qualifier("userProfileTemplate")
    RestTemplate restTemplate;


    @Override
    public ResponseEntity<?> approveByIds(String authHeader, UpdateDefermentCommand command) {

        repository.findAllByDefermentId(command.getDefermentIds()).forEach(d -> {
            d.setStatus(ApprovalStatus.APPROVED.value());
            d.setApprovalRemarks(command.getRemarks());
            d.setUpdatedBy(command.getUserId());
            d.setUpdatedDate(new Date());
            repository.save(d);
            try {
                sendEmailAndSms(authHeader, d.getUserId(), ApprovalStatus.APPROVED.value());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        });

        return ResponseEntity.ok(new MessageResponse("Approved successfully"));
    }

    @Override
    public ResponseEntity<?> rejectByIds(String authHeader, @Valid UpdateDefermentCommand command) {
        repository.findAllByDefermentId(command.getDefermentIds()).forEach(d -> {
            d.setStatus(ApprovalStatus.REJECTED.value());
            d.setApprovalRemarks(command.getRemarks());
            d.setUpdatedBy(command.getUserId());
            d.setUpdatedDate(new Date());
            repository.save(d);
            try {
                sendEmailAndSms(authHeader, d.getUserId(), ApprovalStatus.REJECTED.value());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        });

        return ResponseEntity.ok(new MessageResponse("Rejected successfully"));

    }

    @Override
    public ResponseEntity<?> saveToDraft(String authHeader, UpdateDefermentCommand command) {
        repository.findAllByDefermentId(command.getDefermentIds()).forEach(d -> {
            d.setStatus(command.getStatus());
            d.setApprovalRemarks(command.getRemarks());
            d.setUpdatedBy(command.getUserId());
            d.setUpdatedDate(new Date());
            repository.save(d);
        });

        return ResponseEntity.ok(new MessageResponse("Saved successfully"));
    }

    @Override
    public ResponseEntity<?> reviewRevertById(String authHeader, @Valid ReviewDefermentCommand command) throws Exception {
        final String[] caseNumber = {null};
        final BigInteger[] userIdDB = {null};
        repository.findByDefermentId(command.getDefermentId()).ifPresent(d -> {
            if (d.getStatus().equals(ApprovalStatus.PENDING.value())) {
                caseNumber[0] = d.getCaseNumber();
                userIdDB[0] = d.getUserId();
                d.setStatus(command.getStatus());
                if (command.getStatus().equals(ApprovalStatus.REVIEWED.value())) {
                    d.setMailStatus(MailSentStatus.NOT_SENT.value());
                }
                d.setReviewerRemarks(command.getReviewRemarks());
                d.setReviewerId(command.getUserId());
                d.setUpdatedBy(command.getUserId());
                d.setUpdatedDate(new Date());
                repository.save(d);
            }
        });

        auditRepository.save(
                mapper.mapToEntityAudit(repository.findByDefermentId(command.getDefermentId()).get(), command.getUserId())
        );

        sendReviewRevertEmailAndSms(authHeader, command.getUserId(), caseNumber[0], command.getStatus(), userIdDB[0], command.getReviewRemarks());

        return ResponseEntity.ok(new MessageResponse("Reviewed/Reverted successfully"));
    }

    @Override
    public ResponseEntity<?> approveRejectById(String authHeader, @Valid ReviewDefermentCommand command) throws Exception {
        final BigInteger[] reviewerIdDB = {null};
        final String[] caseNumber = {null};
        repository.findByDefermentId(command.getDefermentId()).ifPresent(d -> {
            if (d.getStatus().equals(ApprovalStatus.REVIEWED.value())) {
                reviewerIdDB[0] = d.getReviewerId();
                caseNumber[0] = d.getCaseNumber();
                d.setStatus(command.getStatus());
                d.setApprovalRemarks(command.getReviewRemarks());
                d.setApproverId(command.getUserId());
                d.setUpdatedBy(command.getUserId());
                d.setUpdatedDate(new Date());
                repository.save(d);
            }
        });

        auditRepository.save(
                mapper.mapToEntityAudit(repository.findByDefermentId(command.getDefermentId()).get(), command.getUserId())
        );

        sendReviewRevertEmailAndSms(authHeader, command.getUserId(), caseNumber[0], command.getStatus(), reviewerIdDB[0], command.getReviewRemarks());


        return ResponseEntity.ok(new MessageResponse("Approved/Rejected successfully"));
    }

    @Override
    public ResponseEntity<?> mailSendToApplicant(String authHeader, ReviewDefermentCommand command) {
        repository.findByDefermentId(command.getDefermentId()).ifPresent(d -> {
            if (d.getStatus().equals(ApprovalStatus.APPROVED.value())||d.getStatus().equals(ApprovalStatus.REJECTED.value())) {
                d.setStatus(command.getStatus());
                d.setApprovalRemarks(command.getReviewRemarks());
                d.setApproverId(command.getUserId());
                d.setUpdatedBy(command.getUserId());
                d.setUpdatedDate(new Date());
                repository.save(d);
            }
        });

        return ResponseEntity.ok(new MessageResponse("Mail send successfully"));

    }

    private void sendEmailAndSms(String authHeader, BigInteger userId, Character status) throws Exception {
        ApplicationContext context = new AnnotationConfigApplicationContext(ApplicationProperties.class);
        ApplicationProperties properties = context.getBean(ApplicationProperties.class);

        HttpEntity<String> httpRequest = headerToken.tokenHeader(authHeader);

        String userUrl = properties.getUserProfileById() + userId;
        ResponseEntity<UserProfileDto> userResponse = restTemplate.exchange(userUrl, HttpMethod.GET, httpRequest, UserProfileDto.class);
        String emailMessage = "";
        String subject = "";

        if (status.equals(ApprovalStatus.APPROVED.value())) {
            subject = "Approved for Deferment";
            emailMessage = "Deferment from Gyalsung Training\n" +
                    "\n" +
                    "Dear " + Objects.requireNonNull(userResponse.getBody()).getFullName() + ",\n" +
                    "\n" +
                    "We are pleased to inform you that you have been granted deferment from Gyalsung training. The deferment is valid only for one year. You will be notified about the Gyalsung registration for next upcoming Gyalsung training when you are on deferment. Please follow the instructions and complete the pre-enlistment procedure as soon as you get the registration notice.If the circumstance necessitates you for further deferment, please contact 00000 or visit Gyalsung HQ with all your supporting documents. \n" +
                    "\n" +
                    "Sending you our prayers for your quick recovery/better situation. We look forward to seeing you in the next upcoming Gyalsung training. \n" +
                    "\n" +
                    " Warm Regards, \n" +
                    "\n" +
                    " Gyalsung HQ \n";

        } else {
            subject = "Rejection for Deferment";
            emailMessage = "Dear " + Objects.requireNonNull(userResponse.getBody()).getFullName() + ",\n" +
                    "\n" +
                    "Thank you for your recent application. We regret to inform you that your deferment has not been approved by the reviewing committee. For further clarification please contact us at 00000 or visit us at Gyalsung HQ. Since your deferment is not approved, you are liable for your Gyalsung Training. Please complete all the pre-listment procedure and be ready for your Gyalsung training.  \n" +
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
                Objects.requireNonNull(userResponse.getBody()).getMobileNo(),
                null,
                null);

        //todo get data from properties
        addToQueue.addToQueue("email", eventBus);
        addToQueue.addToQueue("sms", eventBus);
    }

    private void sendReviewRevertEmailAndSms(String authHeader, BigInteger userId, String caseNumber, Character status, BigInteger studentId, String reviewRemarks) throws Exception {
        ApplicationContext context = new AnnotationConfigApplicationContext(ApplicationProperties.class);
        ApplicationProperties properties = context.getBean(ApplicationProperties.class);

        HttpEntity<String> httpRequest = headerToken.tokenHeader(authHeader);

        String userUrl = properties.getUserProfileById() + (status.equals(ApprovalStatus.REVIEWED.value()) ? userId : studentId);
        ResponseEntity<UserProfileDto> userResponse = restTemplate.exchange(userUrl, HttpMethod.GET, httpRequest, UserProfileDto.class);
        String fullName = Objects.requireNonNull(userResponse.getBody()).getFullName();
        String cid = userResponse.getBody().getCid();
        String emailMessage = "";
        String subject = "";

        if (status.equals(ApprovalStatus.REVIEWED.value())) {
            subject = "Reviewed for Deferment";
            emailMessage = "Please review the deferment application of " + Objects.requireNonNull(userResponse.getBody()).getFullName() + "\n" +
                    " with case number " + caseNumber + "\n" +
                    "\n" +
                    "and proceed with either its approval or rejection.\n";
            mailToOperator.sendMailToOperator(fullName, cid, properties, httpRequest, "deferment", caseNumber, RoleStatus.SENIOR_DEFERMENT_OFFICER.value(), emailMessage, subject);

        } else {

            if (status.equals(ApprovalStatus.APPROVED.value()) ) {
                subject = "Deferment Approved";
                emailMessage = "The deferment application bearing case number " + caseNumber + " has been granted approval. Kindly proceed with the required course of action.\n";
            } else if(status.equals(ApprovalStatus.REJECTED.value())){
                subject = "Deferment Rejected";
                emailMessage = "The deferment application bearing case number " + caseNumber + " has been rejected. Kindly proceed with the required course of action.\n";
            }else {
                subject = "Request for Missing Document";
                emailMessage = "Dear " + Objects.requireNonNull(userResponse.getBody()).getFullName() + ",\n" +
                        "\n" +
                        "To continue processing your deferment application, we kindly request that you submit " + reviewRemarks + " document. Please add the required documents to the deferment page. Thank you.\n";
            }


            EventBus eventBus = EventBus.withId(
                    Objects.requireNonNull(userResponse.getBody()).getEmail(),
                    null,
                    null,
                    emailMessage,
                    subject,
                    Objects.requireNonNull(userResponse.getBody()).getMobileNo(),
                    null,
                    null);

            //todo get from properties
            addToQueue.addToQueue("email", eventBus);
            addToQueue.addToQueue("sms", eventBus);
        }


    }

}
