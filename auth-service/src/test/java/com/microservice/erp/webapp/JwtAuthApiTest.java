package com.microservice.erp.webapp;

import org.junit.Test;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = {"com.microservice.erp.controllers"
        ,"com.microservice.erp.services"})
public class JwtAuthApiTest {
    @Test
    public void contextLoads() {
    }
}