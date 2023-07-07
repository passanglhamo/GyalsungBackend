package com.microservice.erp.domain.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;
import java.util.Date;

@Getter
@Setter
public class ExcelGridIdDTO {
    private BigInteger id;
    private Integer hospitalId;
    private Date appointmentDate;
}
