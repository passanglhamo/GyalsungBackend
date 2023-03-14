package com.microservice.erp.domain.helper;

import lombok.SneakyThrows;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;


import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.util.Properties;

public class SmsSender {
    public static void sendSms(String destinationNumber, String message) {
        new Thread() {
            @SneakyThrows
            public void run() {
                Resource resource = new ClassPathResource("/smsConfig/smsConfig.properties");
                Properties props = PropertiesLoaderUtils.loadProperties(resource);
                final String smsUrl = props.getProperty("sms.url");
//                RestTemplate restTemplate = new RestTemplate();
//                restTemplate.exchange(smsUrl + destinationNumber + "&msg=" + message, HttpMethod.GET, null, String.class);
                String finalMessage = message.replaceAll("\\s+", "+");
                Unirest.setTimeouts(0, 0);
                HttpResponse<String> response = Unirest.get(smsUrl + destinationNumber + "&msg=" + finalMessage).asString();
            }
        }.start();
    }
}
