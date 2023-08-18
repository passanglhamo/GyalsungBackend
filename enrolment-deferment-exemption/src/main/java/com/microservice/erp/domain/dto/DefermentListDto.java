package com.microservice.erp.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigInteger;
import java.util.Collection;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DefermentListDto {
    private BigInteger id;
    private String defermentYear;
    private BigInteger userId;
    private BigInteger reasonId;
    private String approvalRemarks;
    private Character status;
    private Character mailStatus;
    private String remarks;
    private MultipartFile[] proofDocuments;
    private Collection<DefermentFileDto> defermentFileDtos;

    private String fullName;
    private String cid;
    private Date dob;
    private Character gender;
    private Date applicationDate;
    private String caseNumber;
    private Collection<DefermentDto> defermentList;
    private Date createdDate;
    private BigInteger reviewerId;
    private BigInteger approverId;
    private String reviewerFullName;
    private String approverFullName;


    public static DefermentListDto withId(
            BigInteger id,
            String defermentYear,
            BigInteger userId,
            BigInteger reasonId,
            String approvalRemarks,
            Character status,
            Character mailStatus,
            String remarks,
            MultipartFile[] proofDocuments,
            Collection<DefermentFileDto> defermentFileDtos,
            String fullName,
            String cid,
            Date dob,
            Character gender,
            Date applicationDate,
            String caseNumber,
            Collection<DefermentDto> defermentList,
            Date createdDate,
            BigInteger reviewerId,
            BigInteger approverId,
            String reviewerFullName,
            String approverFullName) {
        return new DefermentListDto(
                id,
                defermentYear,
                userId,
                reasonId,
                approvalRemarks,
                status,
                mailStatus,
                remarks,
                proofDocuments,
                defermentFileDtos,
                fullName,
                cid,
                dob,
                gender,
                applicationDate,
                caseNumber,
                defermentList,
                createdDate,
                reviewerId,
                approverId,
                reviewerFullName,
                approverFullName);
    }
}
