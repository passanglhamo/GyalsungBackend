package com.microservice.erp.services.impl;

import com.jcraft.jsch.*;
import com.microservice.erp.domain.dto.*;
import com.microservice.erp.domain.entities.DefermentFileInfo;
import com.microservice.erp.domain.entities.DefermentInfo;
import com.microservice.erp.domain.helper.ApprovalStatus;
import com.microservice.erp.domain.helper.MailSentStatus;
import com.microservice.erp.domain.mapper.DefermentMapper;
import com.microservice.erp.domain.repositories.IDefermentFileInfoRepository;
import com.microservice.erp.domain.repositories.IDefermentInfoAuditRepository;
import com.microservice.erp.domain.repositories.IDefermentInfoRepository;
import com.microservice.erp.services.iServices.IReadDefermentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReadDefermentService implements IReadDefermentService {
    private final IDefermentInfoRepository repository;
    private final IDefermentFileInfoRepository fileInfoRepository;
    private final IDefermentInfoAuditRepository auditRepository;
    private final DefermentMapper mapper;
    private final DefermentExemptionValidation defermentExemptionValidation;
    private final UserInformationService userInformationService;
    private final HeaderToken headerToken;

    @Autowired
    @Qualifier("trainingManagementTemplate")
    RestTemplate restTemplate;

    @Autowired
    @Qualifier("userProfileTemplate")
    RestTemplate userRestTemplate;

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
                                                                              Character gender, String cid, String caseNumber) {
        ApplicationContext context = new AnnotationConfigApplicationContext(ApplicationProperties.class);
        ApplicationProperties properties = context.getBean(ApplicationProperties.class);

        HttpEntity<String> httpRequest = headerToken.tokenHeader(authHeader);
        // Define the desired date format
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM dd, yyyy", Locale.ENGLISH);


        defermentYear = defermentYear.isEmpty() ? null : defermentYear;
        cid = cid.isEmpty() ? null : cid;
        caseNumber = caseNumber.isEmpty() ? null : caseNumber;

        List<UserProfileDto> userProfileDtos;

        List<DefermentDto> defermentDtoList = repository.getDefermentListByToDateStatus(defermentYear, status, gender, reasonId, caseNumber)
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
                    .filter(deferment -> item.getUserId().equals(deferment.getUserId()))
                    .max(Comparator.comparing(DefermentDto::getId))
                    .orElse(null);
            List<DefermentDto> defermentList = repository.findAllByUserIdOrderByDefermentIdDesc(item.getId())
                    .stream()
                    .map(mapper::mapToDomain)
                    .collect(Collectors.toUnmodifiableList());
            DefermentListDto defermentData = new DefermentListDto();
            if (!Objects.isNull(defermentDto)) {
                defermentData.setId(defermentDto.getId());
                defermentData.setRemarks(defermentDto.getRemarks());
                defermentData.setApprovalRemarks(defermentDto.getApprovalRemarks());
                defermentData.setReviewerRemarks(defermentDto.getReviewerRemarks());
                defermentData.setDefermentYear(defermentDto.getDefermentYear());
                defermentData.setStatus(defermentDto.getStatus());
                defermentData.setFullName(Objects.requireNonNull(item).getFullName());
                defermentData.setCid(Objects.requireNonNull(item).getCid());
                defermentData.setDob(Objects.requireNonNull(item).getDob());
                defermentData.setGender(Objects.requireNonNull(item).getGender());
                defermentData.setGenderName(Objects.requireNonNull(item).getGender().equals('M') ? "Male" : "Female");
                defermentData.setReasonId(defermentDto.getReasonId());
                defermentData.setApplicationDate(defermentDto.getApplicationDate());
                // Format the date to the desired format
                String formattedDate = dateFormat.format(defermentDto.getApplicationDate());
                defermentData.setApplicationDateInString(formattedDate);
                defermentData.setUserId(defermentDto.getUserId());
                defermentData.setCaseNumber(defermentDto.getCaseNumber());
                defermentData.setMailStatus(defermentDto.getMailStatus());
                defermentData.setDefermentList(defermentList);
                defermentData.setStatusName(ApprovalStatus.fromValue(defermentDto.getStatus()).getName());
                defermentData.setMailStatusName(Objects.isNull(defermentDto.getMailStatus()) ? "" : MailSentStatus.fromValue(defermentDto.getMailStatus()).getName());
                String reasonUrl = properties.getReasonById() + defermentDto.getReasonId();
                ResponseEntity<ReasonDto> reasonDto = restTemplate.exchange(reasonUrl, HttpMethod.GET, httpRequest, ReasonDto.class);
                defermentData.setReasonName(Objects.requireNonNull(reasonDto.getBody()).getReasonName());
                defermentData.setIsMedicalReason(Objects.requireNonNull(reasonDto.getBody()).getIsMedicalReason());
                if (!Objects.isNull(defermentDto.getReviewerId())) {
                    String userUrl = properties.getUserProfileById() + defermentDto.getReviewerId();
                    ResponseEntity<UserProfileDto> userResponse = userRestTemplate.exchange(userUrl, HttpMethod.GET, httpRequest, UserProfileDto.class);
                    defermentData.setReviewerFullName(Objects.requireNonNull(Objects.requireNonNull(userResponse).getBody()).getFullName());
                }

                if (!Objects.isNull(defermentDto.getApproverId())) {
                    String userUrl = properties.getUserProfileById() + defermentDto.getApproverId();
                    ResponseEntity<UserProfileDto> userResponse = userRestTemplate.exchange(userUrl, HttpMethod.GET, httpRequest, UserProfileDto.class);
                    defermentData.setApproverFullName(Objects.requireNonNull(Objects.requireNonNull(userResponse).getBody()).getFullName());
                }

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

        List<DefermentDto> defermentInfosList = repository.findAllByDefermentYearAndUserIdAndStatusAndMailStatus(defermentYear, userId, ApprovalStatus.REJECTED.value(),
                MailSentStatus.NOT_SENT.value())
                .stream()
                .map(mapper::mapToDomain)
                .collect(Collectors.toUnmodifiableList());

        if (defermentInfosList.isEmpty()) {
            List<Character> statuses = Arrays.asList(ApprovalStatus.APPROVED.value(),
                    ApprovalStatus.PENDING.value(), ApprovalStatus.REVIEWED.value(), ApprovalStatus.REVERTED.value(),
                    ApprovalStatus.PENDING_APPROVAL.value(), ApprovalStatus.PENDING_REJECTION.value());

            return repository.findAllByDefermentYearAndUserIdAndStatusIn(defermentYear, userId, statuses)
                    .stream()
                    .map(mapper::mapToDomain)
                    .collect(Collectors.toUnmodifiableList());
        }

        return defermentInfosList;


    }

    @Override
    public ResponseEntity<?> getDefermentListByUserId(BigInteger userId) {
        return ResponseEntity.ok(repository.findAllByUserIdOrderByDefermentIdDesc(userId)
                .stream()
                .map(mapper::mapToDomain)
                .collect(Collectors.toUnmodifiableList()));
    }


    @Override
    public List<DefermentDto> getDefermentAuditListByDefermentId(String authHeader, BigInteger defermentId) {
        List<DefermentDto> defermentDtoList = auditRepository.findAllByDefermentIdOrderByDefermentAuditIdDesc(defermentId)
                .stream()
                .map(mapper::mapToAuditDomain)
                .collect(Collectors.toUnmodifiableList());
        List<BigInteger> userIdsVal = defermentDtoList
                .stream()
                .map(DefermentDto::getUserId)
                .collect(Collectors.toList());
        List<UserProfileDto> userProfileDtos = userInformationService.getUserInformationByListOfIds(userIdsVal, authHeader);

        defermentDtoList.forEach(defermentDto -> {
            UserProfileDto userProfileDto = userProfileDtos.stream()
                    .filter(user -> defermentDto.getUserId().equals(user.getUserId()))
                    .max(Comparator.comparing(UserProfileDto::getId))
                    .orElse(null);
            if (!Objects.isNull(userProfileDto)) {
                defermentDto.setFullName(userProfileDto.getFullName());
                defermentDto.setCid(userProfileDto.getCid());
                defermentDto.setDob(userProfileDto.getDob());
                defermentDto.setGender(userProfileDto.getGender());
            }
        });

        return defermentDtoList;
    }

    @Override
    public List<DefermentFileDto> getDefermentFileListByDefermentId(BigInteger defermentId) {
       return fileInfoRepository.findAllByDeferment(repository.findByDefermentId(defermentId).get())
                .stream()
                .map(mapper::mapToFileDomain)
                .collect(Collectors.toUnmodifiableList());
    }


}
