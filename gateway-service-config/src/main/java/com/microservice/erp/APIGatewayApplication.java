package com.microservice.erp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author Rajib Kumer Ghosh
 *
 */

@SpringBootApplication
@EnableEurekaClient
@ComponentScan(basePackages = {"com.microservice.erp.config"})
public class APIGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(APIGatewayApplication.class, args);
	}

}
