package com.microservice.erp.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
public class HospitalBookingDateDto {
    private BigInteger id;
    private BigInteger hospitalId;
    private Date appointmentDate;
    private Integer amSlots;
    private Integer amSlotsLeft;
    private Integer pmSlots;
    private Integer pmSlotsLeft;
    private Character status;

    public static HospitalBookingDateDto withId(
            BigInteger id,
            BigInteger hospitalId,
            Date appointmentDate,
            Integer amSlots,
            Integer amSlotsLeft,
            Integer pmSlots,
            Integer pmSlotsLeft,
            Character status) {
        return new HospitalBookingDateDto(
                id,
                hospitalId,
                appointmentDate,
                amSlots,
                amSlotsLeft,
                pmSlots,
                pmSlotsLeft,
                status);
    }

}
