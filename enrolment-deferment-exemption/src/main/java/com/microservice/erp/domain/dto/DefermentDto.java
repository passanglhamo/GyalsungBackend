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
    private  Date fromDate;
    private  BigInteger userId;
    private  BigInteger reasonId;
    private  String approvalRemarks;
    private  Date toDate;
    private  Character status;
    private  String remarks;
    private  MultipartFile[] proofDocuments;
    private  Collection<DefermentFileDto> defermentFileDtos;

    private String fullName;
    private String cid;
    private Date dob;
    private String sex;

        public static DefermentDto withId(
                BigInteger id,
                Date fromDate,
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
                String sex) {
            return new DefermentDto(
                    id,
                    fromDate,
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
                    sex);
        }


}
