package com.microservice.erp.domain.mapper;


import com.microservice.erp.domain.dto.DefermentDto;
import com.microservice.erp.domain.dto.DefermentFileDto;
import com.microservice.erp.domain.entities.DefermentFileInfo;
import com.microservice.erp.domain.entities.DefermentFileInfoAudit;
import com.microservice.erp.domain.entities.DefermentInfo;
import com.microservice.erp.domain.entities.DefermentInfoAudit;
import com.microservice.erp.domain.helper.ApprovalStatus;
import com.microservice.erp.domain.repositories.IDefermentFileInfoRepository;
import com.microservice.erp.domain.repositories.IDefermentInfoRepository;
import com.microservice.erp.services.iServices.ICreateDefermentService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;

@Component
@RequiredArgsConstructor
public class DefermentMapper {
    private final IDefermentInfoRepository repository;
    private final IDefermentFileInfoRepository fileRepository;

    public DefermentInfo mapToEntity(HttpServletRequest request, ICreateDefermentService.CreateDefermentCommand command,
                                     String caseNumber) {

        DefermentInfo deferment = new ModelMapper().map(command, DefermentInfo.class);
        Optional<DefermentInfo> defermentDb = repository.findByDefermentId(command.getId());
        deferment.setStatus(ApprovalStatus.PENDING.value());
        deferment.setApplicationDate((!Objects.isNull(command.getId())) ? defermentDb.get().getApplicationDate() : new Date());
        deferment.setCaseNumber((!Objects.isNull(command.getId())) ? defermentDb.get().getCaseNumber() : caseNumber);
        deferment.setCreatedBy((!Objects.isNull(command.getId())) ? defermentDb.get().getCreatedBy() : command.getUserId());
        deferment.setCreatedDate((!Objects.isNull(command.getId())) ? defermentDb.get().getCreatedDate() : new Date());
        if (!Objects.isNull(command.getId())) {
            Set<DefermentFileInfo> defermentFileDb = new HashSet<>(fileRepository.findAllByDeferment(defermentDb.get()));
            deferment.setReviewerRemarks(defermentDb.get().getReviewerRemarks());
            deferment.setReviewerId(defermentDb.get().getReviewerId());
            deferment.setApproverId(defermentDb.get().getApproverId());
            deferment.setApprovalRemarks(defermentDb.get().getApprovalRemarks());
            deferment.setFiles(defermentDb.get().getFiles());
            deferment.setUpdatedBy(command.getUserId());
            deferment.setUpdatedDate(new Date());
            deferment.setFiles(defermentFileDb);
        }

        // Always add new files, even during an update operation
        if (!Objects.isNull(command.getProofDocuments())) {
            if(Objects.isNull(deferment.getFiles())){
                deferment.setFiles(getFiles(deferment, command.getProofDocuments(),command.getUserId()));
            }else{
                deferment.getFiles().addAll(getFiles(deferment, command.getProofDocuments(),command.getUserId()));
            }
        }


        return deferment;
    }

    private Set<DefermentFileInfo>  getFiles(DefermentInfo deferment,MultipartFile[] proofDocuments,BigInteger userId){
       return Arrays.stream(proofDocuments)
                .map(t -> {
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
                                userId,
                                new Date(),
                                compressedBytes
                        );
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toSet());
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


}
