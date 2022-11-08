package com.microservice.erp.domain.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
public class ResponseDto {
    private String hospitalName;
    private String dzongkhagName;
    private Date appointmentDate;
    private String startTime;
    private String endTime;
}
