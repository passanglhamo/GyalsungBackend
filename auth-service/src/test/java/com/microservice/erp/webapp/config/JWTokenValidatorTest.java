package com.microservice.erp.webapp.config;

import com.infoworks.lab.jjwt.JWTHeader;
import com.infoworks.lab.jjwt.JWTPayload;
import com.infoworks.lab.jjwt.JWTValidator;
import com.infoworks.lab.jwtoken.definition.TokenProvider;
import com.infoworks.lab.jwtoken.services.JWTokenProvider;
import org.junit.Assert;
import org.junit.Test;

import java.util.Date;

public class JWTokenValidatorTest {

    @Test
    public void test(){
        JWTPayload payload = new JWTPayload().setSub("userName")
                .setIss("userName")
                .setIat(new Date().getTime())
                .setExp(TokenProvider.defaultTokenTimeToLive().getTimeInMillis())
                .addData("/new/account","false")
                .addData("/isValidToken","true");
        //
        TokenProvider token = new JWTokenProvider("SecretKeyToGenJWTs")
                .setPayload(payload)
                .setHeader(new JWTHeader().setTyp("round").setKid("112223344"));
        //
        String tokenKey = token.generateToken(TokenProvider.defaultTokenTimeToLive());
        System.out.println(tokenKey);
        //
        JWTValidator validator = new JWTValidator();
        boolean isTrue = validator.isValid(tokenKey, "SecretKeyToGenJWTs");
        Assert.assertTrue(isTrue);
    }

    //@Test
    public void testStatic(){
        String tokenKey = "eyJraWQiOiIxMTIyMjMzNDQiLCJ0eXAiOiJyb3VuZCIsImFsZyI6IkhTNTEyIn0.eyJpYXQiOjE2MzAxNTc1NzcyNDQsIm5iZiI6MCwiZXhwIjoxNjMwMTYxMTc3MjU2LCJpc3MiOiJ1c2VyTmFtZSIsInN1YiI6InVzZXJOYW1lIiwiZGF0YSI6eyIvaXNWYWxpZFRva2VuIjoidHJ1ZSIsIi9uZXcvYWNjb3VudCI6ImZhbHNlIn19.d_etJSQUiONLB0NlxnVja9n_-dJn2HtzRXtD0q3hG5Ls7KFTqHj29n4P4Je82sh86oFaeFhMK4jd1hwFRkCrQA";
        //
        JWTValidator validator = new JWTValidator();
        boolean isTrue = validator.isValid(tokenKey, "SecretKeyToGenJWTs");
        Assert.assertTrue(isTrue);
    }

}