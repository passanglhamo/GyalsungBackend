package com.microservice.erp.domain.dto;

import lombok.Getter;
import lombok.Setter;
import java.math.BigInteger;
import java.util.Date;

@Setter
@Getter
public class HospitalBookingDetailsDto {
    private BigInteger hospitalId;
    private Date appointmentDate;
    private Character amPm;
    private BigInteger userId;
    private String hospitalName;
}
