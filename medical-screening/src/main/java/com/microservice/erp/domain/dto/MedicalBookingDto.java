package com.microservice.erp.domain.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class MedicalBookingDto {
    private Long userId;
    private Long hospitalId;
    private Long dzongkhagId;
//    private String appointmentDate;
    private Long scheduleTimeId;
     private List<MedicalQuestionDto> medicalQuestionDtos;
}
