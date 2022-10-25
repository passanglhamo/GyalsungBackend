package com.microservice.erp.services.impl.common;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class HeaderToken {

    public HttpEntity<String> tokenHeader() {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + "static-token");
        return new HttpEntity<String>(headers);
    }

}
