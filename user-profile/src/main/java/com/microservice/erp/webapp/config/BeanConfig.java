package com.microservice.erp.webapp.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.client.RestTemplate;

/**
 * @author Rajib Kumer Ghosh
 *
 */

@Configuration
@PropertySource("classpath:application.yml")
public class BeanConfig {

    @Bean
    ObjectMapper getMapper(){
        return new ObjectMapper();
    }

    @Bean("authTemplate") @LoadBalanced
    public RestTemplate getTemplate(@Value("${auth.service.url}") String url) {
        return new RestTemplateBuilder()
                .rootUri(url)
                .build();
    }

    @Bean("trainingManagementTemplate") @LoadBalanced
    public RestTemplate getTrainingManagementTemplate(@Value("${training.management.service.url}") String url) {
        return new RestTemplateBuilder()
                .rootUri(url)
                .build();
    }

    @Bean("datahubTokenTemplate") @LoadBalanced
    public RestTemplate getDatahubTokenTemplate(@Value("${getDatahubToken.endPointURL}") String url) {
        return new RestTemplateBuilder()
                .rootUri(url)
                .build();
    }

}
