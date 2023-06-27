package com.microservice.erp.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigInteger;
import java.util.Date;


@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class EarlyEnlistmentDto {
    //region private variables
    private BigInteger enlistmentId;
    private BigInteger userId;
    private Character status;
    private String enlistmentYear;
    private Date applicationDate;
    private Character gender;
    private String cid;
    private String fullName;
    private Character parentConsentStatus;
    private Integer dzongkhagId;
    private EarlyEnlistmentMedBookingDto earlyEnlistmentMedBookingDto;
    //endregion

    public static EarlyEnlistmentDto withId(
            BigInteger enlistmentId,
            BigInteger userId,
            Character status,
            String enlistmentYear,
            Date applicationDate,
            Character gender,
            String cid,
            String fullName,
            Character parentConsentStatus,
            Integer dzongkhagId,
            EarlyEnlistmentMedBookingDto earlyEnlistmentMedBookingDto) {
        return new EarlyEnlistmentDto(
                enlistmentId,
                userId,
                status,
                enlistmentYear,
                applicationDate,
                gender,
                cid,
                fullName,
                parentConsentStatus,
                dzongkhagId,
                earlyEnlistmentMedBookingDto
        );
    }

}
