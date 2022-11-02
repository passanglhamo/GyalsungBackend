package com.microservice.erp.domain.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class MedicalBookingDto {
    private Long hospitalId;
    private Long dzongkhagId;
    private String appointmentDate;
    private String appointmentTime;
     private List<MedicalQuestionDto> medicalQuestionDtos;
}
