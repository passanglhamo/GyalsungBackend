package com.microservice.erp.domain.dto.deferment;

import io.swagger.models.auth.In;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * Created by : Passang Lhamo
 * Creation Date : 25/09/2022
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DefermentDto {
    private  Long id;
    private  Date fromDate;
    private  Long userId;
    private  Long reasonId;
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
                Long id,
                Date fromDate,
                Long userId,
                Long reasonId,
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
