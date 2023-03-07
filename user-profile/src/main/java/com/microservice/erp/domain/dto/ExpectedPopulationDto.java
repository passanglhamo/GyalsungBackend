package com.microservice.erp.domain.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ExpectedPopulationDto {

    private String dob;
    private String cidNo;
    private String name;
    private String gender;
    private Boolean isRegistered;
}
