package com.microservice.erp.services.impl;

import com.microservice.erp.domain.dto.DefermentDto;
import com.microservice.erp.domain.dto.DefermentListDto;
import com.microservice.erp.domain.dto.UserProfileDto;
import com.microservice.erp.domain.entities.DefermentInfo;
import com.microservice.erp.domain.helper.ApprovalStatus;
import com.microservice.erp.domain.mapper.DefermentMapper;
import com.microservice.erp.domain.repositories.IDefermentInfoRepository;
import com.microservice.erp.services.iServices.IReadDefermentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReadDefermentService implements IReadDefermentService {
    private final IDefermentInfoRepository repository;
    private final DefermentMapper mapper;
    private final DefermentExemptionValidation defermentExemptionValidation;
    private final UserInformationService userInformationService;

    @Autowired
    @Qualifier("trainingManagementTemplate")
    RestTemplate restTemplate;

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


        List<UserProfileDto> userProfileDtos = userInformationService.getUserInformationByListOfIds(userIdsVal, authHeader);


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
    public List<DefermentListDto> getDefermentListByDefermentYearReasonStatus(String authHeader, String defermentYear,
                                                                              BigInteger reasonId, Character status,
                                                                              Character gender, String cid) {

        defermentYear = defermentYear.isEmpty() ? null : defermentYear;
        cid = cid.isEmpty() ? null : cid;

        List<UserProfileDto> userProfileDtos;

        List<DefermentDto> defermentDtoList = repository.getDefermentListByToDateStatus(defermentYear, status, gender, reasonId)
                .stream()
                .map(mapper::mapToDomain)
                .collect(Collectors.toUnmodifiableList());

        List<BigInteger> userIdsVal;
        List<DefermentListDto> defermentDtos = new ArrayList<>();

        if (!Objects.isNull(cid)) {
            userProfileDtos = userInformationService.getUserInformationByPartialCid(cid, authHeader);
        } else {
            userIdsVal = defermentDtoList
                    .stream()
                    .map(DefermentDto::getUserId)
                    .collect(Collectors.toList());
            userProfileDtos = userInformationService.getUserInformationByListOfIds(userIdsVal, authHeader);

        }
        userProfileDtos.forEach(item -> {
            DefermentDto defermentDto = defermentDtoList.stream()
                    .filter(deferment -> item.getId().equals(deferment.getUserId()))
                    .max(Comparator.comparing(DefermentDto::getId))
                    .orElse(null);
            List<DefermentDto> defermentList = repository.findAllByUserIdOrderByIdDesc(item.getId())
                    .stream()
                    .map(mapper::mapToDomain)
                    .collect(Collectors.toUnmodifiableList());
            DefermentListDto defermentData = new DefermentListDto();
            if (!Objects.isNull(defermentDto)) {
                defermentData.setId(defermentDto.getId());
                defermentData.setRemarks(defermentDto.getRemarks());
                defermentData.setApprovalRemarks(defermentDto.getApprovalRemarks());
                defermentData.setDefermentYear(defermentDto.getDefermentYear());
                defermentData.setStatus(defermentDto.getStatus());
                defermentData.setFullName(Objects.requireNonNull(item).getFullName());
                defermentData.setCid(Objects.requireNonNull(item).getCid());
                defermentData.setDob(Objects.requireNonNull(item).getDob());
                defermentData.setGender(Objects.requireNonNull(item).getGender());
                defermentData.setDefermentFileDtos(defermentDto.getDefermentFileDtos());
                defermentData.setReasonId(defermentDto.getReasonId());
                defermentData.setApplicationDate(defermentDto.getApplicationDate());
                defermentData.setUserId(defermentDto.getUserId());
                defermentData.setDefermentList(defermentList);
            }
            defermentDtos.add(defermentData);
        });
        return defermentDtos;
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
                .getDefermentAndExemptValidation(userId, 'D', "");
    }

    @Override
    public List<DefermentDto> getApprovedListByDefermentYearAndUserId(String authHeader, String defermentYear, BigInteger userId) {
        List<Character> statuses = Arrays.asList(ApprovalStatus.APPROVED.value(),
                ApprovalStatus.PENDING.value());

        return repository.findAllByDefermentYearAndUserIdAndStatusIn(defermentYear, userId, statuses)
                .stream()
                .map(mapper::mapToDomain)
                .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public ResponseEntity<?> getDefermentListByUserId(BigInteger userId) {
        return ResponseEntity.ok(repository.findAllByUserIdOrderByIdDesc(userId)
                .stream()
                .map(mapper::mapToDomain)
                .collect(Collectors.toUnmodifiableList()));
    }

}
