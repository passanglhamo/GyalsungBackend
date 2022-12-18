package com.microservice.erp.webapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.microservice.erp.controllers"
        , "com.microservice.erp.services"
        , "com.microservice.erp.webapp.config"
        , "com.microservice.erp.domain"})
public class MedicalScreeningApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(MedicalScreeningApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(MedicalScreeningApplication.class);
    }
}
