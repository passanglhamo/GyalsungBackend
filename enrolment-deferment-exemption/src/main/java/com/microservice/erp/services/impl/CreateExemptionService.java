package com.microservice.erp.services.impl;

import com.microservice.erp.domain.dto.ApplicationProperties;
import com.microservice.erp.domain.dto.EventBus;
import com.microservice.erp.domain.dto.UserProfileDto;
import com.microservice.erp.domain.helper.ApprovalStatus;
import com.microservice.erp.domain.helper.MessageResponse;
import com.microservice.erp.domain.helper.StatusResponse;
import com.microservice.erp.domain.mapper.ExemptionMapper;
import com.microservice.erp.domain.repositories.IExemptionInfoRepository;
import com.microservice.erp.services.iServices.ICreateExemptionService;
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
public class CreateExemptionService implements ICreateExemptionService {
    private final IExemptionInfoRepository repository;
    private final ExemptionMapper mapper;
    private final AddToQueue addToQueue;
    private final HeaderToken headerToken;
    private final DefermentExemptionValidation defermentExemptionValidation;
    Integer fileLength = 5;
    private final MailToOperator mailToOperator;


    @Autowired
    @Qualifier("userProfileTemplate")
    RestTemplate restTemplate;


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
            if (responseMessage.getStatus().equals(ApprovalStatus.APPROVED.value())) {
                return new ResponseEntity<>(new MessageResponse(responseMessage.getMessage()), HttpStatus.ALREADY_REPORTED);

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
        ApplicationContext context = new AnnotationConfigApplicationContext(ApplicationProperties.class);
        ApplicationProperties properties = context.getBean(ApplicationProperties.class);

        HttpEntity<String> httpRequest = headerToken.tokenHeader(authTokenHeader);

        String userUrl = properties.getUserProfileById() + userId;
        ResponseEntity<UserProfileDto> userResponse = restTemplate.exchange(userUrl, HttpMethod.GET, httpRequest, UserProfileDto.class);
        String fullName = Objects.requireNonNull(userResponse.getBody()).getFullName();
        String cid = userResponse.getBody().getCid();

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
                Objects.requireNonNull(userResponse.getBody()).getMobileNo(),
                null,
                null);

        //todo Need to get from properties
        addToQueue.addToQueue("email", eventBus);
        addToQueue.addToQueue("sms", eventBus);

        mailToOperator.sendMailToOperator(fullName,cid,properties,httpRequest,"deferment");

    }
}
