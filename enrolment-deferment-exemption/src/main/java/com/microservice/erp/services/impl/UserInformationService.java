package com.microservice.erp.services.impl;

import com.microservice.erp.domain.dto.ApplicationProperties;
import com.microservice.erp.domain.dto.UserProfileDto;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class UserInformationService {

    @Autowired
    @Qualifier("userProfileTemplate")
    RestTemplate restTemplate;

    private final HeaderToken headerToken;


    public List<UserProfileDto> getUserInformationByListOfIds(List<BigInteger> userIdsVal, String authHeader) {
        ApplicationContext context = new AnnotationConfigApplicationContext(ApplicationProperties.class);
        ApplicationProperties properties = context.getBean(ApplicationProperties.class);

        HttpEntity<String> request = headerToken.tokenHeader(authHeader);
        String userIds = userIdsVal.stream().map(String::valueOf).collect(Collectors.joining(","));

        String userUrl = properties.getUserProfileByIds() + userIds;
        ResponseEntity<List<UserProfileDto>> userResponse = restTemplate.exchange(userUrl, HttpMethod.GET, request, new ParameterizedTypeReference<List<UserProfileDto>>() {
        });
        return userResponse.getBody();
    }

    public List<UserProfileDto> getUserInformationByPartialCid(String cid, String authHeader) {
        ApplicationContext context = new AnnotationConfigApplicationContext(ApplicationProperties.class);
        ApplicationProperties properties = context.getBean(ApplicationProperties.class);
        HttpEntity<String> request = headerToken.tokenHeader(authHeader);
        String userUrl = properties.getUsersByPartialCid() + cid;
        ResponseEntity<List<UserProfileDto>> userResponse = restTemplate.exchange(userUrl, HttpMethod.GET, request, new ParameterizedTypeReference<>() {
        });

        return userResponse.getBody();
    }

    public UserProfileDto getUserInformationById(BigInteger id, String authHeader) {
        ApplicationContext context = new AnnotationConfigApplicationContext(ApplicationProperties.class);
        ApplicationProperties properties = context.getBean(ApplicationProperties.class);

        HttpEntity<String> request = headerToken.tokenHeader(authHeader);

        String userUrl = properties.getUserProfileById() + id;
        ResponseEntity<UserProfileDto> userResponse = restTemplate.exchange(userUrl, HttpMethod.GET, request, new ParameterizedTypeReference<UserProfileDto>() {
        });
        return userResponse.getBody();
    }
}
