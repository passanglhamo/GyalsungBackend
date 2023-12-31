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
public class ExemptionListDto {
    private String exemptionYear;
    private BigInteger id;
    private BigInteger userId;
    private BigInteger reasonId;
    private String approvalRemarks;
    private Character status;
    private String remarks;
    private MultipartFile[] proofDocuments;
    private Collection<ExemptionFileDto> exemptionFileDtos;

    private String fullName;
    private String cid;
    private Date dob;
    private Character gender;
    private Date applicationDate;
    private String caseNumber;
    private Collection<ExemptionDto> exemptionList;

    public static ExemptionListDto withId(
            String exemptionYear,
            BigInteger id,
            BigInteger userId,
            BigInteger reasonId,
            String approvalRemarks,
            Character status,
            String remarks,
            MultipartFile[] proofDocuments,
            Collection<ExemptionFileDto> exemptionFileDtos,
            String fullName,
            String cid,
            Date dob,
            Character gender,
            Date applicationDate,
            String caseNumber,
            Collection<ExemptionDto> exemptionList) {
        return new ExemptionListDto(
                exemptionYear,
                id,
                userId,
                reasonId,
                approvalRemarks,
                status,
                remarks,
                proofDocuments,
                exemptionFileDtos,
                fullName,
                cid,
                dob,
                gender,
                applicationDate,
                caseNumber,
                exemptionList);
    }
}
