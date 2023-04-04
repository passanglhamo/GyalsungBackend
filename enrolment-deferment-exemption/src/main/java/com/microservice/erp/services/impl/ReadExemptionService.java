package com.microservice.erp.services.impl;

import com.microservice.erp.domain.dto.DefermentDto;
import com.microservice.erp.domain.dto.ExemptionDto;
import com.microservice.erp.domain.dto.UserProfileDto;
import com.microservice.erp.domain.entities.ExemptionInfo;
import com.microservice.erp.domain.mapper.ExemptionMapper;
import com.microservice.erp.domain.repositories.IExemptionInfoRepository;
import com.microservice.erp.services.iServices.IReadExemptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReadExemptionService implements IReadExemptionService {
    private final IExemptionInfoRepository repository;
    private final ExemptionMapper mapper;
    private final DefermentExemptionValidation defermentExemptionValidation;
    private final UserInformationService userInformationService;


    @Override
    public List<ExemptionDto> getAllExemptionList(String authHeader) {
        List<ExemptionDto> exemptionDtoList = repository.findAll()
                .stream()
                .map(mapper::mapToDomain)
                .collect(Collectors.toUnmodifiableList());

        List<BigInteger> userIdsVal = exemptionDtoList
                .stream()
                .map(ExemptionDto::getUserId)
                .collect(Collectors.toList());


        List<UserProfileDto> userProfileDtos = userInformationService.getUserInformationByListOfIds(userIdsVal, authHeader);

        //Merge exemption and user information
        exemptionDtoList.forEach(defermentDto -> {
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

        return exemptionDtoList;

    }

    @Override
    public List<ExemptionDto> getExemptionListByCriteria(String authHeader, String exemptionYear, Character status,
                                                         BigInteger reasonId, Character gender, String cid) {

        exemptionYear = exemptionYear.isEmpty() ? null : exemptionYear;
        cid = cid.isEmpty() ? null : cid;

        List<ExemptionDto> exemptionDtoList = repository.getExemptionListByToDateStatus(exemptionYear,status,gender, reasonId)
                .stream()
                .map(mapper::mapToDomain)
                .collect(Collectors.toUnmodifiableList());
        List<BigInteger> userIdsVal;
        List<ExemptionDto> exemptionDtos = new ArrayList<>();
        List<UserProfileDto> userProfileDtos;

        if (!Objects.isNull(cid)) {
            userProfileDtos = userInformationService.getUserInformationByPartialCid(cid, authHeader);
        } else {
            userIdsVal = exemptionDtoList
                    .stream()
                    .map(ExemptionDto::getUserId)
                    .collect(Collectors.toList());
            userProfileDtos = userInformationService.getUserInformationByListOfIds(userIdsVal, authHeader);

        }

        //Merge exemption and user information
        userProfileDtos.forEach(item -> {
            ExemptionDto exemptionDto = exemptionDtoList
                    .stream()
                    .filter(exemption -> item.getId().equals(exemption.getUserId()))
                    .findAny()
                    .orElse(null);
            ExemptionDto exemptionData = new ExemptionDto();
            if (!Objects.isNull(exemptionDto)) {
                exemptionData.setId(exemptionDto.getId());
                exemptionData.setApprovalRemarks(exemptionDto.getApprovalRemarks());
                exemptionData.setStatus(exemptionDto.getStatus());
                exemptionData.setFullName(Objects.requireNonNull(item).getFullName());
                exemptionData.setCid(Objects.requireNonNull(item).getCid());
                exemptionData.setDob(Objects.requireNonNull(item).getDob());
                exemptionData.setGender(Objects.requireNonNull(item).getGender());
                exemptionData.setExemptionFileDtos(exemptionDto.getExemptionFileDtos());
                exemptionData.setReasonId(exemptionDto.getReasonId());
            }
            exemptionDtos.add(exemptionData);
        });
        return exemptionDtos;

    }

    @Override
    public ResponseEntity<?> getExemptionByUserId(BigInteger userId) {
        ExemptionInfo exemptionInfo = Objects.isNull(repository.getExemptionByUserId(userId)) ? null :
                repository.getExemptionByUserId(userId);
        return ResponseEntity.ok(exemptionInfo);
    }

    @Override
    public ResponseEntity<?> getExemptionValidation(BigInteger userId) {
        return defermentExemptionValidation.getDefermentAndExemptValidation(userId);
    }
}
