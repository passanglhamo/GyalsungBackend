package com.microservice.erp.webapp.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig {

    @Bean
    ObjectMapper getMapper(){
        return new ObjectMapper();
    }

}
