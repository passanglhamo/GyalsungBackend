package com.microservice.erp.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigInteger;
import java.util.Collection;
import java.util.Date;

/**
 * Created by : Passang Lhamo
 * Creation Date : 25/09/2022
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DefermentDto {
    private BigInteger id;
    private String defermentYear;
    private BigInteger userId;
    private BigInteger reasonId;
    private String approvalRemarks;
    private String reviewerRemarks;
    private Character status;
    private Character mailStatus;
    private String remarks;
    private MultipartFile[] proofDocuments;
    private Collection<DefermentFileDto> defermentFileDtos;

    private String fullName;
    private String cid;
    private Date dob;
    private Character gender;
    private String genderName;
    private Date applicationDate;
    private String caseNumber;
    private Date createdDate;
    private BigInteger reviewerId;
    private BigInteger approverId;
    private String reviewerFullName;


    public static DefermentDto withId(
            BigInteger id,
            String defermentYear,
            BigInteger userId,
            BigInteger reasonId,
            String approvalRemarks,
            String reviewerRemarks,
            Character status,
            Character mailStatus,
            String remarks,
            MultipartFile[] proofDocuments,
            Collection<DefermentFileDto> defermentFileDtos,
            String fullName,
            String cid,
            Date dob,
            Character gender,
            String genderName,
            Date applicationDate,
            String caseNumber,
            Date createdDate,
            BigInteger reviewerId,
            BigInteger approverId,
            String reviewerFullName) {
        return new DefermentDto(
                id,
                defermentYear,
                userId,
                reasonId,
                approvalRemarks,
                reviewerRemarks,
                status,
                mailStatus,
                remarks,
                proofDocuments,
                defermentFileDtos,
                fullName,
                cid,
                dob,
                gender,
                genderName,
                applicationDate,
                caseNumber,
                createdDate,
                reviewerId,
                approverId,
                reviewerFullName);
    }


}
