package com.microservice.erp.webapp.config;

import com.infoworks.lab.jjwt.JWTValidator;
import com.infoworks.lab.jjwt.TokenValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.http.HttpServletRequest;

@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode=ScopedProxyMode.TARGET_CLASS)
public class JWTokenValidator extends JWTValidator {

    private static Logger LOG = LoggerFactory.getLogger(JWTokenValidator.class.getSimpleName());

    private HttpServletRequest request;

    public JWTokenValidator(@Autowired HttpServletRequest request) {
        this.request = request;
    }

    protected String getHeaderValue(String key){
        if (request == null) return "";
        String value = request.getHeader(key);
        if (value == null){
            value = request.getParameter(key);
        }
        return value;
    }

    @Override
    public Boolean isValid(String token, String... args) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(token, headers);

        String userUrl = "http://localhost:8084/api/user/profile/auth/validateToken?token=" + token;

        ResponseEntity<?> userResponse = restTemplate.exchange(userUrl, HttpMethod.GET, entity, String.class);

        return userResponse.getStatusCode().value() == 200;
    }

    @Override
    public String getUserID(String token, String... args) {
        if (token == null || token.isEmpty()){
            token = TokenValidator.parseToken(getHeaderValue(HttpHeaders.AUTHORIZATION), "Bearer ");
        }
        return super.getUserID(token, args);
    }

    @Override
    public String getIssuer(String token, String... args) {
        if (token == null || token.isEmpty()){
            token = TokenValidator.parseToken(getHeaderValue(HttpHeaders.AUTHORIZATION), "Bearer ");
        }
        return super.getIssuer(token, args);
    }

}
