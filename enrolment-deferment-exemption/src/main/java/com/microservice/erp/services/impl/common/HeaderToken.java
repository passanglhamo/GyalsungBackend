package com.microservice.erp.services.impl.common;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class HeaderToken {

    public HttpEntity<String> tokenHeader(String authHeader) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", authHeader);
        return new HttpEntity<String>(headers);
    }

}
