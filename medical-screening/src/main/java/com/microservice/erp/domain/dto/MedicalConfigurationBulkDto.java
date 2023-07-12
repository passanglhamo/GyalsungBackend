package com.microservice.erp.domain.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class MedicalConfigurationBulkDto {
    private List<MedicalConfigurationDto> medicalConfigurationDtos;
}
