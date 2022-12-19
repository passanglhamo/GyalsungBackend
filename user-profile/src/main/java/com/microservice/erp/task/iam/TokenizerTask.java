package com.microservice.erp.task.iam;

import com.infoworks.lab.beans.tasks.nuts.AbstractTask;
import com.infoworks.lab.jjwt.JWTHeader;
import com.infoworks.lab.jjwt.JWTPayload;
import com.infoworks.lab.jwtoken.definition.TokenProvider;
import com.infoworks.lab.jwtoken.services.JWTokenProvider;
import com.infoworks.lab.rest.models.Response;
import com.it.soul.lab.sql.query.models.Property;
import com.microservice.erp.domain.entities.SaUser;
import org.springframework.security.core.authority.AuthorityUtils;

import java.time.Duration;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.Random;

public abstract class TokenizerTask extends AbstractTask<Response, Response> {

    public TokenizerTask(Property... properties) {
        super(properties);
    }

    protected String getToken(SaUser exist, Calendar timeToLive) {
        Integer kid = getRandomSecretKey(exist.getSecrets());
        String secret = getSecret(exist, kid);
        //
        JWTHeader header = new JWTHeader().setTyp("round").setKid(kid.toString());
        JWTPayload payload = new JWTPayload()
                .setSub(exist.getUsername())
                .setIss(exist.getUsername())
                .setIat(new Date().getTime())
                .setExp(timeToLive.getTimeInMillis())
                .addData("mobile", exist.getMobileNo())
                .addData("email", exist.getEmail());
        //Adding Roles to Claim:
        if (exist.getAuthorities().size() > 0){
            String[] roles = AuthorityUtils.authorityListToSet(exist.getAuthorities()).toArray(new String[0]);
            payload = payload.addData("roles", String.join(",", roles));
        }
        //
        TokenProvider token = new JWTokenProvider(secret)
                .setHeader(header)
                .setPayload(payload);
        return token.generateToken(timeToLive);
    }

    protected Integer getRandomSecretKey(Map<Integer, String> secrets){
        int randIndex = new Random().nextInt(secrets.size());
        Integer kid = secrets.keySet().toArray(new Integer[0])[randIndex];
        return kid;
    }

    protected String getSecret(SaUser exist, Integer kid) {
        // Taking last 12 Char from the encrypted-password,
        // So that if user change his/her password then old token will be auto-expired.
        String secret = exist.getSecrets().get(kid)
                + exist.getPassword().substring(exist.getPassword().length() - 13);
        return secret;
    }

    protected Calendar getTimeToLive() {
        try {
            Object ttlObj = getPropertyValue("ttl");
            if (ttlObj == null) ttlObj = getPropertyValue("timeToLive");
            if (ttlObj == null)
                return TokenProvider.defaultTokenTimeToLive();
            //
            long timeInMillis = Long.valueOf(ttlObj.toString());
            Duration duration = Duration.ofMillis(timeInMillis);
            if (duration.isZero() || duration.isNegative())
                return TokenProvider.defaultTokenTimeToLive();
            //
            Calendar timeToLive = Calendar.getInstance();
            timeToLive.add(Calendar.MILLISECOND, Long.valueOf(duration.toMillis()).intValue());
            return timeToLive;
        } catch (Exception e) {
            return TokenProvider.defaultTokenTimeToLive();
        }
    }

}
