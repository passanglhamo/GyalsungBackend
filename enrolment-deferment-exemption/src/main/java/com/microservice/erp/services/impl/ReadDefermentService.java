package com.microservice.erp.services.impl;

import com.microservice.erp.domain.dto.DefermentDto;
import com.microservice.erp.domain.dto.UserProfileDto;
import com.microservice.erp.domain.entities.DefermentFileInfo;
import com.microservice.erp.domain.entities.DefermentInfo;
import com.microservice.erp.domain.helper.FileUploadToExternalLocation;
import com.microservice.erp.domain.helper.ResponseMessage;
import com.microservice.erp.domain.helper.SystemDataInt;
import com.microservice.erp.domain.mapper.DefermentMapper;
import com.microservice.erp.domain.repositories.IDefermentFileInfoRepository;
import com.microservice.erp.domain.repositories.IDefermentInfoRepository;
import com.microservice.erp.services.iServices.IReadDefermentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

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
    private final DefermentExemptionValidation defermentExemptionValidation;
    private final UserInformationService userInformationService;

    @Override
    public List<DefermentDto> getAllDefermentList(String authHeader) {
        //To get deferment list
        List<DefermentDto> defermentDtoList = repository.findAll()
                .stream()
                .map(mapper::mapToDomain)
                .collect(Collectors.toUnmodifiableList());

        List<BigInteger> userIdsVal = defermentDtoList
                .stream()
                .map(DefermentDto::getUserId)
                .collect(Collectors.toList());


        List<UserProfileDto> userProfileDtos = userInformationService.getUserInformationByListOfIds(userIdsVal,authHeader);


        //Merge deferment and user information
        defermentDtoList.forEach(defermentDto -> {
            UserProfileDto userProfileDto = userProfileDtos
                    .stream()
                    .filter(user -> defermentDto.getUserId().equals(user.getId()))
                    .findAny()
                    .orElse(null);
            defermentDto.setFullName(Objects.requireNonNull(userProfileDto).getFullName());
            defermentDto.setCid(Objects.requireNonNull(userProfileDto).getCid());
            defermentDto.setDob(Objects.requireNonNull(userProfileDto).getDob());
            defermentDto.setGender(Objects.requireNonNull(userProfileDto).getGender());
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
    public List<DefermentDto> getDefermentListByToDateStatus(String authHeader, Date toDate, Character status) {
        List<DefermentDto> defermentDtoList = repository.getDefermentListByToDateStatus(
                toDate, status)
                .stream()
                .map(mapper::mapToDomain)
                .collect(Collectors.toUnmodifiableList());
        List<BigInteger> userIdsVal = defermentDtoList
                .stream()
                .map(DefermentDto::getUserId)
                .collect(Collectors.toList());


        List<UserProfileDto> userProfileDtos = userInformationService.getUserInformationByListOfIds(userIdsVal,authHeader);

        //Merge deferment and user information
        defermentDtoList.forEach(defermentDto -> {
            UserProfileDto userProfileDto = userProfileDtos
                    .stream()
                    .filter(user -> defermentDto.getUserId().equals(user.getId()))
                    .findAny()
                    .orElse(null);
            defermentDto.setFullName(Objects.requireNonNull(userProfileDto).getFullName());
            defermentDto.setCid(Objects.requireNonNull(userProfileDto).getCid());
            defermentDto.setDob(Objects.requireNonNull(userProfileDto).getDob());
            defermentDto.setGender(Objects.requireNonNull(userProfileDto).getGender());
        });

        return defermentDtoList;
    }

    @Override
    public ResponseEntity<?> getDefermentByUserId(BigInteger userId) {
        DefermentInfo defermentInfo = Objects.isNull(repository.getDefermentByUserId(userId)) ? null :
                repository.getDefermentByUserId(userId);
        return ResponseEntity.ok(defermentInfo);
    }

    @Override
    public ResponseEntity<?> getDefermentValidation(BigInteger userId) {
        return defermentExemptionValidation
                .getDefermentAndExemptValidation(userId);
    }

}
