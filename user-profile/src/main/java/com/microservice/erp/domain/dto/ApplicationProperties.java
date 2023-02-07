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

    @Value("${auth.service.user.by.userId}")
    private String authServiceToGetUserById;

    @Value("${training.management.geog.by.geogId}")
    private String trainingManGeogByGeogId;

    @Value("${training.management.dzongkhag.by.dzongkhayId}")
    private String trainingManDzongkhagByDzongkhagId;

}
