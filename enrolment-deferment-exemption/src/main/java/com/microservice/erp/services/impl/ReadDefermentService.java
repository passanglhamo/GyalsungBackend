package com.microservice.erp.services.impl;

import com.microservice.erp.domain.dto.DefermentDto;
import com.microservice.erp.domain.dto.UserProfileDto;
import com.microservice.erp.domain.entities.DefermentFileInfo;
import com.microservice.erp.domain.helper.FileUploadToExternalLocation;
import com.microservice.erp.domain.helper.ResponseMessage;
import com.microservice.erp.domain.helper.SystemDataInt;
import com.microservice.erp.domain.mapper.DefermentMapper;
import com.microservice.erp.domain.repositories.IDefermentFileInfoRepository;
import com.microservice.erp.domain.repositories.IDefermentInfoRepository;
import com.microservice.erp.services.iServices.IReadDefermentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
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
            item.setSex(Objects.requireNonNull(userResponse.getBody()).getSex());
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
    public ResponseEntity<?> getDefermentListByToDateStatus(ReadDefermentCommand command) {
        List<DefermentDto> defermentDtoList = repository.getDefermentListByToDateStatus(
                        command.getToDate(), command.getStatus())
                .stream()
                .map(mapper::mapToDomain)
                .collect(Collectors.toUnmodifiableList());

        return ResponseEntity.ok(defermentDtoList);
    }

}
