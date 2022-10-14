package com.microservice.erp.services.impl;

import com.microservice.erp.domain.entities.ApiAccessToken;
import com.microservice.erp.domain.repositories.IApiAccessTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.transaction.Transactional;
import java.io.IOException;
import java.math.BigInteger;
import java.text.ParseException;
import java.util.Base64;
import java.util.Properties;

@Service
@Transactional
public class CitizenDetailApiService {
    @Autowired
    private IApiAccessTokenRepository iApiAccessTokenRepository;

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Autowired
    private RestTemplate restTemplate;


    public ApiAccessToken getApplicationToken() throws ParseException, IOException {
        ApiAccessToken token = iApiAccessTokenRepository.findTop1ByOrderByIdDesc();

        if (null == token) {
            token = generateToken();
        } else {
            float secondsElapsed = (System.currentTimeMillis() - token.getCreated_on().longValue()) / 1000F;
            if (secondsElapsed > token.getExpires_in()) {
                iApiAccessTokenRepository.deleteAll();
                token = generateToken();
            } else {
                return token;
            }
        }
        return token;
    }

    private ApiAccessToken generateToken() throws IOException {

        Resource resource = new ClassPathResource("/apiConfig/dcrcApi.properties");
        Properties props = PropertiesLoaderUtils.loadProperties(resource);
        String dataHubEndPointUrl = props.getProperty("getDatahubToken.endPointURL");
        String consumerKey = props.getProperty("CONSUMER.KEY");
        String consumerSecret = props.getProperty("CONSUMER.SECRET");


        /*ResourceBundle resourceBundle1 = ResourceBundle.getBundle("wsEndPointURL_en_US");
        String dataHubEndPointUrl = resourceBundle1.getString("getDatahubToken.endPointURL");
        String consumerKey = resourceBundle1.getString("CONSUMER.KEY");
        String consumerSecret = resourceBundle1.getString("CONSUMER.SECRET");
        */

//        String consumerKey = "8TSm4oRLfIWOCIAfTrPCb7rCd3ga";
//        String consumerSecret = "zXDkGmn1yFp6jfsw6R4XkigJl0ga";

        ApiAccessToken apiAccessToken = new ApiAccessToken();
        String authStringEnc = Base64.getEncoder().encodeToString((consumerKey + ":" + consumerSecret).getBytes());
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Basic " + authStringEnc);
        HttpEntity<String> request = new HttpEntity<String>(headers);
        ResponseEntity<ApiAccessToken> response = restTemplate.exchange(dataHubEndPointUrl, HttpMethod.POST, request, ApiAccessToken.class);
        apiAccessToken.setAccess_token(response.getBody().getAccess_token());
        apiAccessToken.setExpires_in(response.getBody().getExpires_in());
        apiAccessToken.setScope(response.getBody().getScope());
        apiAccessToken.setToken_type(response.getBody().getToken_type());
        apiAccessToken.setCreated_on(BigInteger.valueOf(System.currentTimeMillis()));
        iApiAccessTokenRepository.save(apiAccessToken);
        return apiAccessToken;
    }
}
