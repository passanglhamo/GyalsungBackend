package com.microservice.erp.domain.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class MoeExpectedPopulationDto {

    private String dob;
    private String cidNo;
    private String name;
    private String gender;
    private Boolean isRegistered;
    private String className;
    private String stream;
    private String schoolName;
    private String section;
 }
