package com.microservice.erp.domain.dto.exemption;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

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
public class ExemptionDto {

    private Long id;
    private Long userId;
    private Long reasonId;
    private String approvalRemarks;
    private Character status;
    private String remarks;
    private MultipartFile[] proofDocuments;
    private Collection<ExemptionFileDto> exemptionFileDtos;

    private String fullName;
    private String cid;
    private Date dob;
    private String sex;

    public static ExemptionDto withId(
            Long id,
            Long userId,
            Long reasonId,
            String approvalRemarks,
            Character status,
            String remarks,
            MultipartFile[] proofDocuments,
            Collection<ExemptionFileDto> exemptionFileDtos,
            String fullName,
            String cid,
            Date dob,
            String sex) {
        return new ExemptionDto(
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
                sex);
    }
}
