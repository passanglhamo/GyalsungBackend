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
//@EnableFeignClients(basePackages = "com.microservice.erp.services")
public class EnrolmentDefermentExemptionApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(EnrolmentDefermentExemptionApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(EnrolmentDefermentExemptionApplication.class);
    }
}
