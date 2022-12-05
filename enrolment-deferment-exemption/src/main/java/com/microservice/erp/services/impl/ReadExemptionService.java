package com.microservice.erp.services.impl;

import com.microservice.erp.domain.dto.ExemptionDto;
import com.microservice.erp.domain.dto.UserProfileDto;
import com.microservice.erp.domain.entities.DefermentInfo;
import com.microservice.erp.domain.entities.ExemptionInfo;
import com.microservice.erp.domain.helper.ApprovalStatus;
import com.microservice.erp.domain.helper.StatusResponse;
import com.microservice.erp.domain.mapper.ExemptionMapper;
import com.microservice.erp.domain.repositories.IDefermentInfoRepository;
import com.microservice.erp.domain.repositories.IExemptionInfoRepository;
import com.microservice.erp.services.iServices.IReadExemptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigInteger;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReadExemptionService implements IReadExemptionService {
    private final IExemptionInfoRepository repository;
    private final IDefermentInfoRepository defermentRepository;
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
            item.setGender(Objects.requireNonNull(userResponse.getBody()).getGender());
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
            item.setGender(Objects.requireNonNull(userResponse.getBody()).getGender());
        });
        return exemptionDtoList;

    }

    @Override
    public ResponseEntity<?> getExemptionByUserId(BigInteger userId) {
        ExemptionInfo exemptionInfo = Objects.isNull(repository.getExemptionByUserId(userId)) ? null :
                repository.getExemptionByUserId(userId);
        return ResponseEntity.ok(exemptionInfo);
    }

    @Override
    public ResponseEntity<?> getExemptionValidation(BigInteger userId) {
        StatusResponse responseMessage = new StatusResponse();
        ExemptionInfo exemptionInfo = repository.getExemptionByUserId(userId);
        if(!Objects.isNull(exemptionInfo)){
            if(exemptionInfo.getStatus().equals(ApprovalStatus.APPROVED.value())){
                responseMessage.setStatus(ApprovalStatus.APPROVED.value());
                responseMessage.setMessage("User is exempted from the gyalsung program.");
                return new ResponseEntity<>(responseMessage, HttpStatus.ALREADY_REPORTED);
            }
            if(exemptionInfo.getStatus().equals(ApprovalStatus.PENDING.value())){
                responseMessage.setStatus(ApprovalStatus.PENDING.value());
                responseMessage.setMessage("There are still some exemption which are not approved. If you continue," +
                        " then the pending exemption will be cancelled.");
                return new ResponseEntity<>(responseMessage, HttpStatus.ALREADY_REPORTED);

            }
        }else{
            DefermentInfo defermentInfo = defermentRepository.getDefermentByUserId(userId);
            if(!Objects.isNull(defermentInfo)){
                if(defermentInfo.getStatus().equals(ApprovalStatus.APPROVED.value())){
                    responseMessage.setStatus(ApprovalStatus.PENDING.value());
                    responseMessage.setMessage("There are still some approved deferment. If you continue," +
                                                        " then the approved deferment will be cancelled.");
                    return new ResponseEntity<>(responseMessage, HttpStatus.ALREADY_REPORTED);

                }
                if(defermentInfo.getStatus().equals(ApprovalStatus.PENDING.value())){
                    responseMessage.setStatus(ApprovalStatus.PENDING.value());
                    responseMessage.setMessage("There are still some deferment which are not approved. If you continue," +
                                                        " then the pending deferment will be cancelled.");
                    return new ResponseEntity<>(responseMessage, HttpStatus.ALREADY_REPORTED);

                }

            }
        }
        return ResponseEntity.ok("No Validation");
    }
}
