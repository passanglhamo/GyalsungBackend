package com.microservice.erp.services.impl;

import com.microservice.erp.domain.dto.ApplicationProperties;
import com.microservice.erp.domain.dto.EventBus;
import com.microservice.erp.domain.dto.UserProfileDto;
import com.microservice.erp.domain.helper.ApprovalStatus;
import com.microservice.erp.domain.helper.MessageResponse;
import com.microservice.erp.domain.helper.RoleStatus;
import com.microservice.erp.domain.helper.StatusResponse;
import com.microservice.erp.domain.mapper.DefermentMapper;
import com.microservice.erp.domain.repositories.IDefermentInfoAuditRepository;
import com.microservice.erp.domain.repositories.IDefermentInfoRepository;
import com.microservice.erp.services.iServices.ICreateDefermentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
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
public class CreateDefermentService implements ICreateDefermentService {

    private final IDefermentInfoRepository repository;
    private final IDefermentInfoAuditRepository auditRepository;
    private final DefermentMapper mapper;
    private final HeaderToken headerToken;
    private final AddToQueue addToQueue;
    private final MailToOperator mailToOperator;
    private final DefermentExemptionValidation defermentExemptionValidation;
    Integer fileLength = 5;
    private final CaseNumberGenerator caseNumberGenerator;

    @Autowired
    @Qualifier("userProfileTemplate")
    RestTemplate restTemplate;

    @Transactional
    public ResponseEntity<?> saveDeferment(HttpServletRequest request, CreateDefermentCommand command) {
        String authTokenHeader = request.getHeader("Authorization");
        repository.getDefermentByUserId(command.getUserId());
        if (!Objects.isNull(command.getProofDocuments())) {
            if (command.getProofDocuments().length > fileLength) {
                return new ResponseEntity<>("You can upload maximum of 5 files.", HttpStatus.ALREADY_REPORTED);
            }
        }
        StatusResponse responseMessage = (StatusResponse) defermentExemptionValidation
                .getDefermentAndExemptValidation(command.getUserId(), 'D', command.getDefermentYear()).getBody();
        if (!Objects.isNull(responseMessage)) {
            if (responseMessage.getStatus().equals(ApprovalStatus.APPROVED.value())) {
                return new ResponseEntity<>(new MessageResponse(responseMessage.getMessage()), HttpStatus.ALREADY_REPORTED);

            }
        }

        String caseNumber = caseNumberGenerator.caseNumberGenerator(authTokenHeader, command.getReasonId(), 'D');

        var deferment = repository.save(
                mapper.mapToEntity(
                        request, command, caseNumber
                )
        );

        auditRepository.save(
                mapper.mapToEntityAudit(deferment, command.getUserId())
        );

        try {
            sendEmailAndSms(authTokenHeader, deferment.getUserId(), caseNumber, command.getIsMedicalReason());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return ResponseEntity.ok(deferment.getCaseNumber());
    }

    private void sendEmailAndSms(String authTokenHeader, BigInteger userId, String caseNumber, Character isMedicalReason) throws Exception {
        ApplicationContext context = new AnnotationConfigApplicationContext(ApplicationProperties.class);
        ApplicationProperties properties = context.getBean(ApplicationProperties.class);

        HttpEntity<String> httpRequest = headerToken.tokenHeader(authTokenHeader);

        String userUrl = properties.getUserProfileById() + userId;
        ResponseEntity<UserProfileDto> userResponse = restTemplate.exchange(userUrl, HttpMethod.GET, httpRequest, UserProfileDto.class);
        String fullName = Objects.requireNonNull(userResponse.getBody()).getFullName();
        String cid = userResponse.getBody().getCid();

        String subject = "Acknowledged for Deferment";

        String emailMessage = "Dear " + Objects.requireNonNull(userResponse.getBody()).getFullName() + ",\n" +
                "\n" +
                "This is to acknowledge the receipt of your deferment application. Case number for your application is " + caseNumber + ". Your deferment application will be reviewed and the outcome of the deferment will be sent to you through your email within 10 days of the submission of your application. If you are not approved for deferment, you will have to complete the Gyalsung pre-enlistment procedure. \n";


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

        Character userType = isMedicalReason.equals('Y') ? RoleStatus.MEDICAL_DEFERMENT_OFFICER.value() : RoleStatus.NON_MEDICAL_DEFERMENT_OFFICER.value();

        String messageGHQ = fullName + " with cid " + cid + " has successfully applied for deferment. The case number for the application is " + caseNumber + ".\n";
        String subjectGHQ = "Gyalsung Registration Pending Approval";

        mailToOperator.sendMailToOperator(fullName, cid, properties, httpRequest, "deferment", caseNumber, userType, messageGHQ, subjectGHQ);


    }


}
