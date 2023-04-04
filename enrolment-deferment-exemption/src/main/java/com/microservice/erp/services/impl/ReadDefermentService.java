package com.microservice.erp.services.impl;

import com.microservice.erp.domain.dto.DefermentDto;
import com.microservice.erp.domain.dto.EnrolmentListDto;
import com.microservice.erp.domain.dto.UserProfileDto;
import com.microservice.erp.domain.entities.DefermentInfo;
import com.microservice.erp.domain.mapper.DefermentMapper;
import com.microservice.erp.domain.repositories.IDefermentInfoRepository;
import com.microservice.erp.services.iServices.IReadDefermentService;
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
public class ReadDefermentService implements IReadDefermentService {
    private final IDefermentInfoRepository repository;
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
    public List<DefermentDto> getDefermentListByDefermentYearReasonStatus(String authHeader, String defermentYear,
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
        List<DefermentDto> defermentDtos = new ArrayList<>();

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
            DefermentDto defermentDto = defermentDtoList
                    .stream()
                    .filter(deferment -> item.getId().equals(deferment.getUserId()))
                    .findAny()
                    .orElse(null);
            DefermentDto defermentData = new DefermentDto();
            if (!Objects.isNull(defermentDto)) {
                defermentData.setId(defermentDto.getId());
                defermentData.setApprovalRemarks(defermentDto.getApprovalRemarks());
                defermentData.setStatus(defermentDto.getStatus());
                defermentData.setFullName(Objects.requireNonNull(item).getFullName());
                defermentData.setCid(Objects.requireNonNull(item).getCid());
                defermentData.setDob(Objects.requireNonNull(item).getDob());
                defermentData.setGender(Objects.requireNonNull(item).getGender());
                defermentData.setDefermentFileDtos(defermentDto.getDefermentFileDtos());
                defermentData.setReasonId(defermentDto.getReasonId());
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
                .getDefermentAndExemptValidation(userId);
    }

}
