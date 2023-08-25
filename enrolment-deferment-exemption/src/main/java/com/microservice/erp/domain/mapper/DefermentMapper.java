package com.microservice.erp.domain.mapper;


import com.microservice.erp.domain.dto.DefermentDto;
import com.microservice.erp.domain.dto.DefermentFileDto;
import com.microservice.erp.domain.entities.DefermentFileInfo;
import com.microservice.erp.domain.entities.DefermentFileInfoAudit;
import com.microservice.erp.domain.entities.DefermentInfo;
import com.microservice.erp.domain.entities.DefermentInfoAudit;
import com.microservice.erp.domain.helper.ApprovalStatus;
import com.microservice.erp.domain.repositories.IDefermentFileInfoAuditRepository;
import com.microservice.erp.domain.repositories.IDefermentFileInfoRepository;
import com.microservice.erp.domain.repositories.IDefermentInfoAuditRepository;
import com.microservice.erp.domain.repositories.IDefermentInfoRepository;
import com.microservice.erp.services.iServices.ICreateDefermentService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.GZIPInputStream;

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
        deferment.setStatus(ApprovalStatus.PENDING.value());
        deferment.setApplicationDate((!Objects.isNull(command.getId())) ? defermentDb.getApplicationDate() : new Date());
        deferment.setCaseNumber((!Objects.isNull(command.getId())) ? defermentDb.getCaseNumber() : caseNumber);
        deferment.setCreatedBy((!Objects.isNull(command.getId())) ? defermentDb.getCreatedBy() : command.getUserId());
        deferment.setCreatedDate((!Objects.isNull(command.getId())) ? defermentDb.getCreatedDate() : new Date());
        if (!Objects.isNull(command.getId())) {
            deferment.setReviewerRemarks(defermentDb.getReviewerRemarks());
            deferment.setReviewerId(defermentDb.getReviewerId());
            deferment.setApproverId(defermentDb.getApproverId());
            deferment.setApprovalRemarks(defermentDb.getApprovalRemarks());
            deferment.setUpdatedBy(command.getUserId());
            deferment.setUpdatedDate(new Date());
        }

        if (!Objects.isNull(command.getProofDocuments())) {
            deferment.setFiles(
                    Arrays.stream(command.getProofDocuments())
                            .map(t ->
                            {

                                try {
                                    byte[] bytes = t.getBytes();
                                    byte[] compressedBytes = compress(bytes);
                                    String filename = t.getOriginalFilename();
                                    Long fileSize = t.getSize();

                                    BigDecimal size = (new BigDecimal(fileSize).divide(new BigDecimal(1024)));
                                    Integer length = (fileSize.toString()).length();

                                    String finalSize = null;
                                    DecimalFormat df = new DecimalFormat("#.00");

                                    if (length <= 6) {
                                        finalSize = df.format(size).toString() + "KB";
                                    } else {
                                        size = (size.divide(new BigDecimal(1024)));
                                        finalSize = df.format(size).toString() + "MB";
                                    }

                                    return new DefermentFileInfo(
                                            finalSize,
                                            filename,
                                            deferment,
                                            command.getUserId(),
                                            new Date(),
                                            compressedBytes
                                    );
                                } catch (Exception e) {
                                    throw new RuntimeException(e);
                                }


                            }).collect(Collectors.toSet())


            );
        }


        return deferment;
    }

    public static byte[] compress(byte[] data) throws Exception {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DeflaterOutputStream deflaterOutputStream = new DeflaterOutputStream(byteArrayOutputStream, new Deflater(Deflater.BEST_COMPRESSION));
        deflaterOutputStream.write(data);
        deflaterOutputStream.finish();
        deflaterOutputStream.close();
        return byteArrayOutputStream.toByteArray();
    }

    public DefermentInfoAudit mapToEntityAudit(DefermentInfo defermentInfo, BigInteger userId) {

        DefermentInfoAudit defermentAudit = new ModelMapper().map(defermentInfo, DefermentInfoAudit.class);
        defermentAudit.setCreatedBy(userId);
        defermentAudit.setCreatedDate(new Date());
        DefermentFileInfoAudit defermentAuditFileDb = fileAuditInfoRepository.findFirstByOrderByDefermentFileAuditIdDesc();
        BigInteger defermentAuditFileId = defermentAuditFileDb == null ? BigInteger.ZERO : defermentAuditFileDb.getDefermentFileAuditId();

        if (!Objects.isNull(defermentInfo.getFiles())) {
            defermentAudit.setFiles(
                    defermentInfo.getFiles().stream().map(t ->
                    {

                        return new DefermentFileInfoAudit(
                                t.getFileSize(),
                                t.getFileName(),
                                defermentAudit,
                                userId,
                                new Date(),
                                t.getFile()
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
                                                ta.getFileSize(),
                                                ta.getFileName(),
                                                ta.getFile()
                                        )
                                )
                                .collect(Collectors.toUnmodifiableSet())
                ,
                null,
                null,
                null,
                deferment.getGender(),
                null,
                deferment.getApplicationDate(),
                deferment.getCaseNumber(),
                deferment.getCreatedDate(),
                deferment.getReviewerId(),
                deferment.getApproverId(),
                null
        );
    }

    public DefermentFileDto mapToFileDomain(DefermentFileInfo defermentFileInfo) {
        return DefermentFileDto.withId(
                defermentFileInfo.getDefermentFileId(),
                defermentFileInfo.getFileSize(),
                defermentFileInfo.getFileName(),
                defermentFileInfo.getFile()
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
                                                ta.getFileSize(),
                                                ta.getFileName(),
                                                ta.getFile()
                                        )
                                )
                                .collect(Collectors.toUnmodifiableSet())
                ,
                null,
                null,
                null,
                defermentInfoAudit.getGender(),
                null,
                defermentInfoAudit.getApplicationDate(),
                defermentInfoAudit.getCaseNumber(),
                defermentInfoAudit.getCreatedDate(),
                null,
                null,
                null
        );
    }


    public byte[] decompress(byte[] data) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try (GZIPInputStream gzipInputStream = new GZIPInputStream(new ByteArrayInputStream(data))) {
            byte[] buffer = new byte[1024];
            int len;
            while ((len = gzipInputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, len);
            }
        }
        return byteArrayOutputStream.toByteArray();
    }

}
