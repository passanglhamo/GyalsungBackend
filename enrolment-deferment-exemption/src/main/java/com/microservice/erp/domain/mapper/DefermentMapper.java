package com.microservice.erp.domain.mapper;


import com.microservice.erp.domain.dto.DefermentDto;
import com.microservice.erp.domain.dto.DefermentFileDto;
import com.microservice.erp.domain.entities.DefermentFileInfo;
import com.microservice.erp.domain.entities.DefermentFileInfoAudit;
import com.microservice.erp.domain.entities.DefermentInfo;
import com.microservice.erp.domain.entities.DefermentInfoAudit;
import com.microservice.erp.domain.helper.ApprovalStatus;
import com.microservice.erp.domain.helper.FileUploadDTO;
import com.microservice.erp.domain.helper.FileUploadToExternalLocation;
import com.microservice.erp.domain.repositories.IDefermentFileInfoAuditRepository;
import com.microservice.erp.domain.repositories.IDefermentFileInfoRepository;
import com.microservice.erp.domain.repositories.IDefermentInfoAuditRepository;
import com.microservice.erp.domain.repositories.IDefermentInfoRepository;
import com.microservice.erp.services.iServices.ICreateDefermentService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class DefermentMapper {
    private final IDefermentInfoRepository repository;
    private final IDefermentInfoAuditRepository auditRepository;
    private final IDefermentFileInfoRepository fileInfoRepository;
    private final IDefermentFileInfoAuditRepository fileAuditInfoRepository;

    public DefermentInfo mapToEntity(HttpServletRequest request, ICreateDefermentService.CreateDefermentCommand command,
                                     String caseNumber) {

        DefermentInfo deferment = new ModelMapper().map(command, DefermentInfo.class);
        DefermentInfo defermentDb = repository.findFirstByOrderByDefermentIdDesc();
        BigInteger defermentId = (!Objects.isNull(command.getId()))?command.getId():defermentDb == null ? BigInteger.ONE : defermentDb.getDefermentId().add(BigInteger.ONE);
        deferment.setDefermentId(defermentId);
        deferment.setStatus(ApprovalStatus.PENDING.value());
        deferment.setApplicationDate((!Objects.isNull(command.getId()))?defermentDb.getApplicationDate():new Date());
        deferment.setCaseNumber((!Objects.isNull(command.getId()))?defermentDb.getCaseNumber():caseNumber);
        deferment.setCreatedBy((!Objects.isNull(command.getId()))?defermentDb.getCreatedBy():command.getUserId());
        deferment.setCreatedDate((!Objects.isNull(command.getId()))?defermentDb.getCreatedDate():new Date());
        if(!Objects.isNull(command.getId())){
            deferment.setReviewerRemarks(defermentDb.getReviewerRemarks());
            deferment.setReviewerId(defermentDb.getReviewerId());
            deferment.setApproverId(defermentDb.getApproverId());
            deferment.setApprovalRemarks(defermentDb.getApprovalRemarks());
            deferment.setUpdatedBy(command.getUserId());
            deferment.setUpdatedDate(new Date());
        }

        DefermentFileInfo defermentFileDb = fileInfoRepository.findFirstByOrderByDefermentFileIdDesc();
        BigInteger defermentFileId = defermentFileDb == null ? BigInteger.ZERO : defermentFileDb.getDefermentFileId();
        final BigInteger[] initialNo = {BigInteger.ZERO};

        if (!Objects.isNull(command.getProofDocuments())) {
            deferment.setFiles(
                    Arrays.stream(command.getProofDocuments())
                            .map(t ->
                            {

                                initialNo[0] = initialNo[0].add(BigInteger.ONE);

                                try {
                                    String filename = t.getOriginalFilename();
                                    Long fileSize = t.getSize();
                                    FileUploadDTO fileUploadDTO = FileUploadToExternalLocation.fileUploadPathRetriever(request);
                                    String fileUrl = fileUploadDTO.getUploadFilePath().concat(filename);
                                    BigDecimal size = (new BigDecimal(fileSize).divide(new BigDecimal(1024)));
                                    Integer length = (fileSize.toString()).length();

                                    FileUploadToExternalLocation.fileUploader(t, filename, "fileConfig/attachFile.properties", request);
                                    String finalSize = null;
                                    DecimalFormat df = new DecimalFormat("#.00");
                                    if (length == 6) {
                                        finalSize = df.format(size).toString() + "KB";
                                    } else {
                                        size = (size.divide(new BigDecimal(1024)));
                                        finalSize = df.format(size).toString() + "MB";
                                    }
                                    return new DefermentFileInfo(
                                            defermentFileId.add(initialNo[0]),
                                            fileUrl,
                                            finalSize,
                                            filename,
                                            deferment,
                                            command.getUserId(),
                                            new Date()
                                    );
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }


                            }).collect(Collectors.toSet())


            );
        }


        return deferment;
    }

    public DefermentInfoAudit mapToEntityAudit(DefermentInfo defermentInfo,BigInteger userId) {

        DefermentInfoAudit defermentAudit = new ModelMapper().map(defermentInfo, DefermentInfoAudit.class);
        DefermentInfoAudit defermentAuditDb = auditRepository.findFirstByOrderByDefermentAuditIdDesc();
        BigInteger defermentAuditId = defermentAuditDb == null ? BigInteger.ONE : defermentAuditDb.getDefermentAuditId().add(BigInteger.ONE);
        defermentAudit.setDefermentAuditId(defermentAuditId);
        defermentAudit.setCreatedBy(userId);
        defermentAudit.setCreatedDate(new Date());
        DefermentFileInfoAudit defermentAuditFileDb = fileAuditInfoRepository.findFirstByOrderByDefermentFileAuditIdDesc();
        BigInteger defermentAuditFileId = defermentAuditFileDb == null ? BigInteger.ZERO : defermentAuditFileDb.getDefermentFileAuditId();
        final BigInteger[] initialNo = {BigInteger.ZERO};

        if (!Objects.isNull(defermentInfo.getFiles())) {
            defermentAudit.setFiles(
                    defermentInfo.getFiles().stream().map(t ->
                    {

                        initialNo[0] = initialNo[0].add(BigInteger.ONE);

                        return new DefermentFileInfoAudit(
                                defermentAuditFileId.add(initialNo[0]),
                                t.getFilePath(),
                                t.getFileSize(),
                                t.getFileName(),
                                defermentAudit,
                                userId,
                                new Date()
                        );


                    }).collect(Collectors.toSet())


            );
        }


        return defermentAudit;
    }

    public DefermentDto mapToDomain(DefermentInfo deferment) {
        return DefermentDto.withId(
                deferment.getDefermentId(),
                deferment.getDefermentYear(),
                deferment.getUserId(),
                deferment.getReasonId(),
                deferment.getApprovalRemarks(),
                deferment.getReviewerRemarks(),
                deferment.getStatus(),
                deferment.getMailStatus(),
                deferment.getRemarks(),
                null,
                deferment.getFiles().size() == 0 ? null :
                        deferment.getFiles()
                                .stream()
                                .map(ta ->
                                        DefermentFileDto.withId(
                                                ta.getDefermentFileId(),
                                                ta.getFilePath(),
                                                ta.getFileSize(),
                                                ta.getFileName()
                                        )
                                )
                                .collect(Collectors.toUnmodifiableSet())
                ,
                null,
                null,
                null,
                deferment.getGender(),
                deferment.getApplicationDate(),
                deferment.getCaseNumber(),
                deferment.getCreatedDate(),
                deferment.getReviewerId(),
                deferment.getApproverId(),
                null
        );
    }

    public DefermentDto mapToAuditDomain(DefermentInfoAudit defermentInfoAudit) {
        return DefermentDto.withId(
                defermentInfoAudit.getDefermentId(),
                defermentInfoAudit.getDefermentYear(),
                defermentInfoAudit.getCreatedBy(),
                defermentInfoAudit.getReasonId(),
                defermentInfoAudit.getApprovalRemarks(),
                defermentInfoAudit.getReviewerRemarks(),
                defermentInfoAudit.getStatus(),
                defermentInfoAudit.getMailStatus(),
                defermentInfoAudit.getRemarks(),
                null,
                defermentInfoAudit.getFiles().size() == 0 ? null :
                        defermentInfoAudit.getFiles()
                                .stream()
                                .map(ta ->
                                        DefermentFileDto.withId(
                                                ta.getDefermentFileAuditId(),
                                                ta.getFilePath(),
                                                ta.getFileSize(),
                                                ta.getFileName()
                                        )
                                )
                                .collect(Collectors.toUnmodifiableSet())
                ,
                null,
                null,
                null,
                defermentInfoAudit.getGender(),
                defermentInfoAudit.getApplicationDate(),
                defermentInfoAudit.getCaseNumber(),
                defermentInfoAudit.getCreatedDate(),
                defermentInfoAudit.getReviewerId(),
                defermentInfoAudit.getApproverId(),
                null
        );
    }
}
