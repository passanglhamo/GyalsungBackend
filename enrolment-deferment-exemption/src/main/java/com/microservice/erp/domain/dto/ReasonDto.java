package com.microservice.erp.domain.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReasonDto {
    private String reasonName;
    private String code;
    private Character defermentExemption;
    private Character isMedicalReason;
    private Character status;


}
