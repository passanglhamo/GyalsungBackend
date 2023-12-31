package com.microservice.erp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author Rajib Kumer Ghosh
 *
 */

@SpringBootApplication
@EnableEurekaClient
@EnableSwagger2
@ComponentScan(basePackages = {
		"com.microservice.erp.controllers"
		, "com.microservice.erp.services"
		, "com.microservice.erp.domain"
		, "com.microservice.erp.webapp.config"})
public class AuthApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuthApplication.class, args);
	}

}
