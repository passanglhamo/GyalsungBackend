package com.microservice.erp.domain.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;
import java.util.List;

@Setter
@Getter
public class MedicalBookingDto {
    private BigInteger userId;
    private BigInteger hospitalId;
    private Long dzongkhagId;
    //    private String appointmentDate;
    private BigInteger scheduleTimeId;
    private List<MedicalQuestionDto> medicalQuestionDtos;
}
