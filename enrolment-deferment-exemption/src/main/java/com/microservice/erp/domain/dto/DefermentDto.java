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
    private Date toDate;
    private Character status;
    private String remarks;
    private MultipartFile[] proofDocuments;
    private Collection<DefermentFileDto> defermentFileDtos;

    private String fullName;
    private String cid;
    private Date dob;
    private Character gender;

    public static DefermentDto withId(
            BigInteger id,
            String defermentYear,
            BigInteger userId,
            BigInteger reasonId,
            String approvalRemarks,
            Date toDate,
            Character status,
            String remarks,
            MultipartFile[] proofDocuments,
            Collection<DefermentFileDto> defermentFileDtos,
            String fullName,
            String cid,
            Date dob,
            Character gender) {
        return new DefermentDto(
                id,
                defermentYear,
                userId,
                reasonId,
                approvalRemarks,
                toDate,
                status,
                remarks,
                proofDocuments,
                defermentFileDtos,
                fullName,
                cid,
                dob,
                gender);
    }


}
