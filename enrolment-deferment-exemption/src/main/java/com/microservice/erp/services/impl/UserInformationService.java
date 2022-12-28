package com.microservice.erp.services.impl;

import com.microservice.erp.domain.dto.UserProfileDto;
import lombok.AllArgsConstructor;
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
    private final HeaderToken headerToken;

    public List<UserProfileDto> getUserInformationByListOfIds(List<BigInteger> userIdsVal,String authHeader){
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> request = headerToken.tokenHeader(authHeader);
        String userIds = userIdsVal.stream().map(String::valueOf).collect(Collectors.joining());

        //Rest template to get user information
        String userUrl = "http://localhost:8084/api/user/profile/userProfile/getProfileInfoByIds?userIds=" + userIds;
        ResponseEntity<List<UserProfileDto>> userResponse = restTemplate.exchange(userUrl, HttpMethod.GET, request, new ParameterizedTypeReference<List<UserProfileDto>>() {
        });
        return userResponse.getBody();
    }
}
