package com.microservice.erp.services.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.microservice.erp.domain.dto.ApplicationProperties;
import com.microservice.erp.domain.dto.EventBus;
import com.microservice.erp.domain.dto.UserProfileDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class MailToOperator {

    private final AddToQueue addToQueue;
    @Autowired
    @Qualifier("userProfileTemplate")
    RestTemplate restTemplate;

    public void sendMailToOperator(String fullName, String cid, ApplicationProperties properties,
                                   HttpEntity<String> httpRequest, String messageContent) throws JsonProcessingException {
        String messageGHQ = fullName+" with cid " +cid+ " has successfully applied for "+messageContent+". \n";
        String subjectGHQ = "Gyalsung Registration Pending Approval";

        String operatorUsers = properties.getOperatorUsers();

        ResponseEntity<List<UserProfileDto>> operatorUserList = restTemplate.exchange(operatorUsers, HttpMethod.GET, httpRequest, new ParameterizedTypeReference<List<UserProfileDto>>() {
        });

        EventBus eventOperatorBus = EventBus.withId(
                null,
                null,
                null,
                messageGHQ,
                subjectGHQ,
                null,
                Objects.requireNonNull(operatorUserList.getBody())
                        .stream()
                        .map(UserProfileDto::getEmail) // Extract the email from each UserProfileDto
                        .collect(Collectors.toList()),
                Objects.requireNonNull(operatorUserList.getBody())
                        .stream()
                        .map(UserProfileDto::getMobileNo) // Extract the mobile number from each UserProfileDto
                        .collect(Collectors.toList()));
        addToQueue.addToQueue("email", eventOperatorBus);
        addToQueue.addToQueue("sms", eventOperatorBus);
    }
}
