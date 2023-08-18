package com.microservice.erp.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigInteger;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EarlyEnlistmentMedBookingDto {
    private BigInteger hospitalBookingId;
    private BigInteger earlyEnlistmentId;
    private BigInteger hospitalId;
    private Date appointmentDate;
    private Character amPm;
    private BigInteger userId;
    private BigInteger signupUserId;
    private String hospitalName;

    public static EarlyEnlistmentMedBookingDto withId(
            BigInteger hospitalBookingId,
            BigInteger earlyEnlistmentId,
            BigInteger hospitalId,
            Date appointmentDate,
            Character amPm,
            BigInteger userId,
            BigInteger signupUserId,
            String hospitalName) {
        return new EarlyEnlistmentMedBookingDto(
                hospitalBookingId,
                earlyEnlistmentId,
                hospitalId,
                appointmentDate,
                amPm,
                userId,
                signupUserId,
                hospitalName
        );
    }
}
