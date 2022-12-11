package com.microservice.erp.webapp.config;

import com.microservice.erp.domain.repositories.UserRepository;
import com.microservice.erp.domain.tasks.iam.CheckTokenValidity;
import com.infoworks.lab.jjwt.JWTValidator;
import com.infoworks.lab.jjwt.TokenValidator;
import com.infoworks.lab.rest.models.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.http.HttpServletRequest;

@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode=ScopedProxyMode.TARGET_CLASS)
public class JWTokenValidator extends JWTValidator {

    private static Logger LOG = LoggerFactory.getLogger(JWTokenValidator.class.getSimpleName());

    private HttpServletRequest request;
    private UserRepository repository;

    public JWTokenValidator(@Autowired HttpServletRequest request
                            , @Autowired UserRepository repository) {
        this.request = request;
        this.repository = repository;
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
        CheckTokenValidity tokenValidity = new CheckTokenValidity(token, repository);
        Response response = tokenValidity.execute(null);
        return response.getStatus() == 200;
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
