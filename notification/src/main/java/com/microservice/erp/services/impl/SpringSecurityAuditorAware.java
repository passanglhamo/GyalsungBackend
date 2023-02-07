package com.microservice.erp.services.impl;

import com.infoworks.lab.jjwt.JWTPayload;
import com.infoworks.lab.jjwt.TokenValidator;
import com.microservice.erp.domain.entities.Username;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.Optional;

@Component
public class SpringSecurityAuditorAware implements AuditorAware<Username> {

    public static void setToken(String token) {
        Authentication auth = new UsernamePasswordAuthenticationToken(token, "");
        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(auth);
    }

    @Override
    public Optional<Username> getCurrentAuditor() {
        JWTPayload jwtPayload = TokenValidator.parsePayload(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString(),
                JWTPayload.class);
        BigInteger userId = new BigInteger(jwtPayload.getSub());
        return Optional.of(new Username(userId.toString()));
    }
}
