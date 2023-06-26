package com.microservice.erp.domain.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;
import java.util.Date;


@Setter
@Getter
public class EarlyEnlistmentDto {
    //region private variables
    private BigInteger enlistmentId;
    private BigInteger userId;
    private Character status;
    private Date applicationDate;
    private Character gender;
    private String cid;
    //endregion

//    public static EarlyEnlistmentDto withId(
//            BigInteger enlistmentId,
//            BigInteger userId,
//            Character status,
//            Date applicationDate,
//            Character gender,
//            String cid) {
//        return new EarlyEnlistmentDto(
//                enlistmentId,
//                userId,
//                status,
//                applicationDate,
//                gender,
//                cid
//        );
//    }

}
