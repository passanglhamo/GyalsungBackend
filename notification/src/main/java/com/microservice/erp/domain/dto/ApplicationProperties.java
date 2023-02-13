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

    @Value("${user.service.getAllUsersEligibleForTraining}")
    private String userGetAllUsersEligibleForTraining;

}
