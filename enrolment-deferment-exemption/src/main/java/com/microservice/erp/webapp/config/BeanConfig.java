package com.microservice.erp.webapp.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.client.RestTemplate;

@Configuration
public class BeanConfig {

    @Bean
    ObjectMapper getMapper(){
        return new ObjectMapper();
    }

    @Bean("trainingManagementTemplate") @LoadBalanced
    public RestTemplate getTrainingManagementTemplate(@Value("${training.management.service.url}") String url) {
        return new RestTemplateBuilder()
                .rootUri(url)
                .build();
    }

    @Primary
    @Bean("userProfileTemplate") @LoadBalanced
    public RestTemplate getUserProfileTemplate(@Value("${user.service.url}") String url) {
        return new RestTemplateBuilder()
                .rootUri(url)
                .build();
    }

    @Bean("medicalTemplate") @LoadBalanced
    public RestTemplate getMedicalTemplate(@Value("${medical.service.url}") String url) {
        return new RestTemplateBuilder()
                .rootUri(url)
                .build();
    }

}
