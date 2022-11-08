package com.microservice.erp.services.helper;

import lombok.SneakyThrows;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

import java.util.Properties;

public class SmsSender {
    public static void sendSms(String destinationNumber, String message) {
        new Thread() {
            @SneakyThrows
            public void run() {
                Resource resource = new ClassPathResource("/smsConfig/smsConfig.properties");
                Properties props = PropertiesLoaderUtils.loadProperties(resource);
                final String smsUrl = props.getProperty("sms.url");
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.exchange(smsUrl + destinationNumber + "&msg=" + message, HttpMethod.GET, null, String.class);
            }
        }.start();
    }
}
