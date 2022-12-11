package com.microservice.erp.webapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
@ComponentScan(basePackages = {
        "com.infoworks.lab.controllers"
        , "com.microservice.erp.services"
        , "com.microservice.erp.domain"
        , "com.microservice.erp.webapp.config"})
public class JwtAuthApi extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(JwtAuthApi.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(JwtAuthApi.class);
    }

}
