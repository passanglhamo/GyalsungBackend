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
public class HospitalBookingDetailsDto {
    private BigInteger id;
    private BigInteger hospitalBookingId;
    private BigInteger hospitalId;
    private Date appointmentDate;
    private Character amPm;
    private BigInteger userId;
    private String hospitalName;

    public static HospitalBookingDetailsDto withId(
            BigInteger id,
            BigInteger hospitalBookingId,
            BigInteger hospitalId,
            Date appointmentDate,
            Character amPm,
            BigInteger userId,
            String hospitalName) {
        return new HospitalBookingDetailsDto(
                id,
                hospitalBookingId,
                hospitalId,
                appointmentDate,
                amPm,
                userId,
                hospitalName
        );
    }
}
