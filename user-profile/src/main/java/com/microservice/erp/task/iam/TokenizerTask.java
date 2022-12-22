package com.microservice.erp.task.iam;

import com.infoworks.lab.beans.tasks.nuts.AbstractTask;
import com.infoworks.lab.jjwt.JWTHeader;
import com.infoworks.lab.jjwt.JWTPayload;
import com.infoworks.lab.jwtoken.definition.TokenProvider;
import com.infoworks.lab.jwtoken.services.JWTokenProvider;
import com.infoworks.lab.rest.models.Response;
import com.it.soul.lab.sql.query.models.Property;
import com.microservice.erp.domain.dao.RoleWiseAccessPermissionDao;
import com.microservice.erp.domain.dto.PermissionListDto;
import com.microservice.erp.domain.entities.SaUser;
import com.microservice.erp.domain.helper.Permission;
import com.microservice.erp.services.impl.RoleWiseAccessPermissionService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.time.Duration;
import java.util.*;

public abstract class TokenizerTask extends AbstractTask<Response, Response> {

    public TokenizerTask(Property... properties) {
        super(properties);
    }

    protected String getToken(SaUser saUser, Calendar timeToLive) {
        Integer kid = getRandomSecretKey(saUser.getSecrets());
        String secret = getSecret(saUser, kid);
        //
        JWTHeader header = new JWTHeader().setTyp("round").setKid(kid.toString());
        JWTPayload payload = new JWTPayload()
                .setSub(saUser.getId().toString())
                .setIss(saUser.getUsername())
                .setIat(new Date().getTime())
                .setExp(timeToLive.getTimeInMillis())
                .addData("mobile", saUser.getMobileNo())
                .addData("email", saUser.getEmail());
        //Adding Roles to Claim:
        if (saUser.getAuthorities().size() > 0) {
            String[] roles = AuthorityUtils.authorityListToSet(saUser.getAuthorities()).toArray(new String[0]);
            payload = payload.addData("roles", String.join(",", roles));
        }

        TokenProvider token = new JWTokenProvider(secret)
                .setHeader(header)
                .setPayload(payload);
        return token.generateToken(timeToLive);
    }

    protected Integer getRandomSecretKey(Map<Integer, String> secrets) {
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
