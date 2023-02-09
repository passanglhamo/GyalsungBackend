package com.microservice.erp.domain.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:application.yml")
@Getter
@Setter
public class ApplicationProperties {

    @Value("${user.service.profile.by.userId}")
    private String userProfileById;

    @Value("${training.management.hospital.by.hospitalId}")
    private String trainingHospitalById;

    @Value("${training.management.mappedHospital.by.hospitalId}")
    private String trainingMappedHospitalById;

}
