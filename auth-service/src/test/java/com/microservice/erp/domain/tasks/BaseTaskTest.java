package com.microservice.erp.domain.tasks;

import com.microservice.erp.domain.entities.User;
import com.infoworks.lab.jjwt.JWTHeader;
import com.infoworks.lab.jjwt.JWTPayload;
import com.infoworks.lab.jwtoken.definition.TokenProvider;
import com.infoworks.lab.jwtoken.services.JWTokenProvider;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class BaseTaskTest {

    protected Map<Integer, String> createRandomSecrets() {
        Map<Integer, String> secrets = new HashMap<>();
        secrets.put(1101, "3ddbfb6e-fd94-467d-a571-bd4795fe9af0"); //{Integer@3326} 1101 -> UUID.randomUUID().toString()
        secrets.put(1102, "860554ad-0deb-454d-b3ee-6f27f69f8be6"); //{Integer@3328} 1102 -> UUID.randomUUID().toString()
        secrets.put(1103, "ed0c31e5-2286-42bd-89a8-30a36d3a9078"); //{Integer@3330} 1103 -> UUID.randomUUID().toString()
        return secrets;
    }

    protected String createToken(User user) {
        //Now existing password matched: lets create the JWT token;
        int randIndex = new Random().nextInt(user.getSecrets().size());
        Integer kid = user.getSecrets().keySet().toArray(new Integer[0])[randIndex];
        String secret = user.getSecrets().get(kid)
                + user.getPassword().substring(user.getPassword().length() - 13);
        //
        JWTHeader header = new JWTHeader().setTyp("round").setKid(kid.toString());
        JWTPayload payload = new JWTPayload()
                .setSub(user.getUsername())
                .setIss(user.getUsername())
                .setIat(new Date().getTime())
                .setExp(TokenProvider.defaultTokenTimeToLive().getTimeInMillis())
                .addData("mobile", user.getCid());
        //
        TokenProvider token = new JWTokenProvider(secret)
                .setHeader(header)
                .setPayload(payload);
        String tokenKey = token.generateToken(TokenProvider.defaultTokenTimeToLive());
        return tokenKey;
    }

}
