package com.microservice.erp.services.impl;

import com.microservice.erp.domain.dto.DefermentDto;
import com.microservice.erp.domain.dto.UserProfileDto;
import com.microservice.erp.domain.entities.DefermentFileInfo;
import com.microservice.erp.domain.entities.DefermentInfo;
import com.microservice.erp.domain.entities.ExemptionInfo;
import com.microservice.erp.domain.helper.*;
import com.microservice.erp.domain.mapper.DefermentMapper;
import com.microservice.erp.domain.repositories.IDefermentFileInfoRepository;
import com.microservice.erp.domain.repositories.IDefermentInfoRepository;
import com.microservice.erp.domain.repositories.IExemptionInfoRepository;
import com.microservice.erp.services.iServices.IReadDefermentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReadDefermentService implements IReadDefermentService {
    private final IDefermentInfoRepository repository;
    private final IDefermentFileInfoRepository repositoryFile;

    private final IExemptionInfoRepository exemptionInfoRepository;
    private final DefermentMapper mapper;
    private final HeaderToken headerToken;

    @Override
    public List<DefermentDto> getAllDefermentList(String authHeader) {
        List<DefermentDto> defermentDtoList = repository.findAll()
                .stream()
                .map(mapper::mapToDomain)
                .collect(Collectors.toUnmodifiableList());

        defermentDtoList.forEach(item -> {
            RestTemplate restTemplate = new RestTemplate();
            HttpEntity<String> request = headerToken.tokenHeader(authHeader);

            String userUrl = "http://localhost:81/api/user/profile/userProfile/getProfileInfo?userId=" + item.getUserId();
            ResponseEntity<UserProfileDto> userResponse = restTemplate.exchange(userUrl, HttpMethod.GET, request, UserProfileDto.class);
            item.setFullName(Objects.requireNonNull(userResponse.getBody()).getFullName());
            item.setCid(Objects.requireNonNull(userResponse.getBody()).getCid());
            item.setDob(Objects.requireNonNull(userResponse.getBody()).getDob());
            item.setGender(Objects.requireNonNull(userResponse.getBody()).getGender());
        });


        return defermentDtoList;

    }

    @Override
    public ResponseEntity<?> downloadFiles(BigInteger defermentId, HttpServletResponse response) {
        DefermentFileInfo defermentFile = repositoryFile.findById(defermentId).get();
        ResponseMessage responseMessage = new ResponseMessage();
        String uploadFilePath = defermentFile.getFilePath();
        String fileName = defermentFile.getFileName();

        try {
            responseMessage = FileUploadToExternalLocation.fileDownloader(fileName, uploadFilePath, response);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        responseMessage.setDto(uploadFilePath);
        responseMessage.setText(uploadFilePath);
        responseMessage.setStatus(SystemDataInt.MESSAGE_STATUS_SUCCESSFUL.value());

        return ResponseEntity.ok(responseMessage);
    }

    @Override
    public List<DefermentDto> getDefermentListByToDateStatus(String authHeader,Date toDate, Character status) {
        List<DefermentDto> defermentDtoList = repository.getDefermentListByToDateStatus(
                        toDate, status)
                .stream()
                .map(mapper::mapToDomain)
                .collect(Collectors.toUnmodifiableList());
        defermentDtoList.forEach(item -> {
            RestTemplate restTemplate = new RestTemplate();
            HttpEntity<String> request = headerToken.tokenHeader(authHeader);

            String userUrl = "http://localhost:81/api/user/profile/userProfile/getProfileInfo?userId=" + item.getUserId();
            ResponseEntity<UserProfileDto> userResponse = restTemplate.exchange(userUrl, HttpMethod.GET, request, UserProfileDto.class);
            item.setFullName(Objects.requireNonNull(userResponse.getBody()).getFullName());
            item.setCid(Objects.requireNonNull(userResponse.getBody()).getCid());
            item.setDob(Objects.requireNonNull(userResponse.getBody()).getDob());
            item.setGender(Objects.requireNonNull(userResponse.getBody()).getGender());
        });


        return defermentDtoList;
    }

    @Override
    public ResponseEntity<?> getDefermentByUserId(BigInteger userId) {
        DefermentInfo defermentInfo = Objects.isNull(repository.getDefermentByUserId(userId))?null:
                repository.getDefermentByUserId(userId);
        return ResponseEntity.ok(defermentInfo);
    }

    @Override
    public ResponseEntity<?> getDefermentValidation(BigInteger userId) {
        StatusResponse responseMessage = new StatusResponse();
        ExemptionInfo exemptionInfo = exemptionInfoRepository.getExemptionByUserId(userId);
        if(!Objects.isNull(exemptionInfo)){
           if(exemptionInfo.getStatus().equals(ApprovalStatus.APPROVED.value())){
               responseMessage.setStatus(ApprovalStatus.APPROVED.value());
               responseMessage.setMessage("User is exempted from the gyalsung program.");
               return new ResponseEntity<>(responseMessage, HttpStatus.ALREADY_REPORTED);
           }
            if(exemptionInfo.getStatus().equals(ApprovalStatus.PENDING.value())){
                responseMessage.setStatus(ApprovalStatus.PENDING.value());
                responseMessage.setMessage("There is still some exemption which are not approved. If you continue," +
                        " then the pending exemption will be cancelled.");
                return new ResponseEntity<>(responseMessage, HttpStatus.ALREADY_REPORTED);

            }
        }else{
            DefermentInfo  defermentInfo = repository.getDefermentByUserId(userId);
            if(!Objects.isNull(defermentInfo)){
                if(defermentInfo.getStatus().equals(ApprovalStatus.APPROVED.value())){
                    responseMessage.setStatus(ApprovalStatus.APPROVED.value());
                    responseMessage.setMessage("User has already applied for deferment.");
                    return new ResponseEntity<>(responseMessage, HttpStatus.ALREADY_REPORTED);

                }
                if(defermentInfo.getStatus().equals(ApprovalStatus.PENDING.value())){
                    responseMessage.setStatus(ApprovalStatus.APPROVED.value());
                    responseMessage.setMessage("There is still some deferment which are not approved. If you continue," +
                                                        " then the pending deferment will be cancelled.");
                    return new ResponseEntity<>(responseMessage, HttpStatus.ALREADY_REPORTED);

                }

            }
        }
        responseMessage.setStatus('I');
        responseMessage.setMessage("No Validation");
        return new ResponseEntity<>(responseMessage, HttpStatus.OK);
    }

}
