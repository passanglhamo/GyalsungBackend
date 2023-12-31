package com.microservice.erp.webapp;

import org.junit.Test;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author Rajib Kumer Ghosh
 *
 */

@Configuration
@ComponentScan(basePackages = {"com.microservice.erp.controllers"
        , "com.microservice.erp.services"})
public class UserProfileApplicationTest {
    @Test
    public void contextLoads() {
        System.out.println("ApplicationContext Loaded Successfully");
    }
}