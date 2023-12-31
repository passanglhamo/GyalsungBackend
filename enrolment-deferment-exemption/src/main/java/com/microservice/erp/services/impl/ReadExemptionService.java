package com.microservice.erp.services.impl;

import com.microservice.erp.domain.dto.ExemptionDto;
import com.microservice.erp.domain.dto.ExemptionListDto;
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
import java.util.Comparator;
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
    public List<ExemptionListDto> getExemptionListByCriteria(String authHeader, String exemptionYear, Character status,
                                                             BigInteger reasonId, Character gender, String cid, String caseNumber) {

        cid = cid.isEmpty() ? null : cid;
        caseNumber = caseNumber.isEmpty() ? null : caseNumber;


        List<ExemptionDto> exemptionDtoList = repository.getExemptionListByToDateStatus(status, gender, reasonId, caseNumber)
                .stream()
                .map(mapper::mapToDomain)
                .collect(Collectors.toUnmodifiableList());
        List<BigInteger> userIdsVal;
        List<UserProfileDto> userProfileDtos;
        List<ExemptionListDto> exemptionListDtos = new ArrayList<>();

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
                    .filter(exemption -> item.getUserId().equals(exemption.getUserId()))
                    .max(Comparator.comparing(ExemptionDto::getId))
                    .orElse(null);
            List<ExemptionDto> exemptionDtoList1 = repository.findAllByUserIdOrderByExemptionIdDesc(item.getId())
                    .stream()
                    .map(mapper::mapToDomain)
                    .collect(Collectors.toUnmodifiableList());
            ExemptionListDto exemptionData = new ExemptionListDto();
            if (!Objects.isNull(exemptionDto)) {
                exemptionData.setId(exemptionDto.getId());
                exemptionData.setRemarks(exemptionDto.getRemarks());
                exemptionData.setApprovalRemarks(exemptionDto.getApprovalRemarks());
                exemptionData.setStatus(exemptionDto.getStatus());
                exemptionData.setFullName(Objects.requireNonNull(item).getFullName());
                exemptionData.setCid(Objects.requireNonNull(item).getCid());
                exemptionData.setDob(Objects.requireNonNull(item).getDob());
                exemptionData.setGender(Objects.requireNonNull(item).getGender());
                exemptionData.setExemptionFileDtos(exemptionDto.getExemptionFileDtos());
                exemptionData.setReasonId(exemptionDto.getReasonId());
                exemptionData.setApplicationDate(exemptionDto.getApplicationDate());
                exemptionData.setUserId(exemptionDto.getUserId());
                exemptionData.setCaseNumber(exemptionDto.getCaseNumber());
                exemptionData.setExemptionList(exemptionDtoList1);

            }
            exemptionListDtos.add(exemptionData);
        });
        return exemptionListDtos;

    }

    @Override
    public ResponseEntity<?> getExemptionByUserId(BigInteger userId) {
        ExemptionInfo exemptionInfo = Objects.isNull(repository.getExemptionByUserId(userId)) ? null :
                repository.getExemptionByUserId(userId);
        return ResponseEntity.ok(exemptionInfo);
    }

    @Override
    public ResponseEntity<?> getExemptionValidation(BigInteger userId) {
        return defermentExemptionValidation.getDefermentAndExemptValidation(userId, 'E', "");
    }

    @Override
    public ResponseEntity<?> getExemptionListByUserId(BigInteger userId) {
        return ResponseEntity.ok(repository.findAllByUserIdOrderByExemptionIdDesc(userId)
                .stream()
                .map(mapper::mapToDomain)
                .collect(Collectors.toUnmodifiableList()));
    }
}
