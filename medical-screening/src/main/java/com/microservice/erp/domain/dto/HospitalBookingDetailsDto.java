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
    private Integer hospitalId;
    private Date appointmentDate;
    private Character amPm;
    private BigInteger userId;
    private String hospitalName;
    private Character status;
    private Date bookingDate;

    public static HospitalBookingDetailsDto withId(
            BigInteger id,
            BigInteger hospitalBookingId,
            Integer hospitalId,
            Date appointmentDate,
            Character amPm,
            BigInteger userId,
            String hospitalName,
            Character status,
            Date bookingDate) {
        return new HospitalBookingDetailsDto(
                id,
                hospitalBookingId,
                hospitalId,
                appointmentDate,
                amPm,
                userId,
                hospitalName,
                status,
                bookingDate
        );
    }
}
