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
public class MedicalConfigurationDto {
    private BigInteger id;
    private Integer hospitalId;
    private Date appointmentDate;
    private Integer amSlots;
    private Integer amSlotsLeft;
    private Integer pmSlots;
    private Integer pmSlotsLeft;
    private Character status;
    private String hospitalName;


    public static MedicalConfigurationDto withId(
            BigInteger id,
            Integer hospitalId,
            Date appointmentDate,
            Integer amSlots,
            Integer amSlotsLeft,
            Integer pmSlots,
            Integer pmSlotsLeft,
            Character status,
            String hospitalName) {
        return new MedicalConfigurationDto(
                id,
                hospitalId,
                appointmentDate,
                amSlots,
                amSlotsLeft,
                pmSlots,
                pmSlotsLeft,
                status,
                hospitalName);
    }

}
