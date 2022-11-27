package com.microservice.erp.services.impl;

import com.microservice.erp.domain.dto.ExemptionDto;
import com.microservice.erp.domain.dto.UserProfileDto;
import com.microservice.erp.domain.mapper.ExemptionMapper;
import com.microservice.erp.domain.repositories.IExemptionInfoRepository;
import com.microservice.erp.services.iServices.IReadExemptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReadExemptionService implements IReadExemptionService {
    private final IExemptionInfoRepository repository;
    private final ExemptionMapper mapper;

    private final HeaderToken headerToken;


    @Override
    public List<ExemptionDto> getAllExemptionList(String authHeader) {
        List<ExemptionDto> exemptionDtoList = repository.findAll()
                .stream()
                .map(mapper::mapToDomain)
                .collect(Collectors.toUnmodifiableList());

        exemptionDtoList.forEach(item -> {
            RestTemplate restTemplate = new RestTemplate();
            HttpEntity<String> request = headerToken.tokenHeader(authHeader);

            String userUrl = "http://localhost:81/api/user/profile/userProfile/getProfileInfo?userId=" + item.getUserId();
            ResponseEntity<UserProfileDto> userResponse = restTemplate.exchange(userUrl, HttpMethod.GET, request, UserProfileDto.class);
            item.setFullName(Objects.requireNonNull(userResponse.getBody()).getFullName());
            item.setCid(Objects.requireNonNull(userResponse.getBody()).getCid());
            item.setDob(Objects.requireNonNull(userResponse.getBody()).getDob());
            item.setSex(Objects.requireNonNull(userResponse.getBody()).getSex());
        });
        return exemptionDtoList;

    }

    @Override
    public List<ExemptionDto> getExemptionListByStatus(String authHeader, Character status) {
        List<ExemptionDto> exemptionDtoList = repository.getExemptionListByToDateStatus(status)
                .stream()
                .map(mapper::mapToDomain)
                .collect(Collectors.toUnmodifiableList());

        exemptionDtoList.forEach(item -> {
            RestTemplate restTemplate = new RestTemplate();
            HttpEntity<String> request = headerToken.tokenHeader(authHeader);

            String userUrl = "http://localhost:81/api/user/profile/userProfile/getProfileInfo?userId=" + item.getUserId();
            ResponseEntity<UserProfileDto> userResponse = restTemplate.exchange(userUrl, HttpMethod.GET, request, UserProfileDto.class);
            item.setFullName(Objects.requireNonNull(userResponse.getBody()).getFullName());
            item.setCid(Objects.requireNonNull(userResponse.getBody()).getCid());
            item.setDob(Objects.requireNonNull(userResponse.getBody()).getDob());
            item.setSex(Objects.requireNonNull(userResponse.getBody()).getSex());
        });
        return exemptionDtoList;

    }
}
