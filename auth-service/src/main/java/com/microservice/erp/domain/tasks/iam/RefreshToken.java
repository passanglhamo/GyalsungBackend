package com.microservice.erp.domain.tasks.iam;

import com.microservice.erp.domain.entities.User;
import com.microservice.erp.domain.repositories.UserRepository;
import com.infoworks.lab.jjwt.JWTHeader;
import com.infoworks.lab.jjwt.JWTPayload;
import com.infoworks.lab.jjwt.TokenValidator;
import com.infoworks.lab.jwtoken.definition.TokenProvider;
import com.infoworks.lab.jwtoken.services.JWTokenProvider;
import com.infoworks.lab.rest.models.Message;
import com.infoworks.lab.rest.models.Response;
import com.it.soul.lab.sql.query.models.Property;

import java.io.IOException;
import java.time.Duration;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class RefreshToken extends TokenizerTask {

    private UserRepository repository;

    public RefreshToken(String token, UserRepository repository) {
        this(token, repository, Duration.ofHours(1));
        this.repository = repository;
    }

    public RefreshToken(String token, UserRepository repository, Duration ttl) {
        super(new Property("token", token), new Property("ttl", ttl.toMillis()));
        this.repository = repository;
    }

    @Override
    public Response execute(Response response) throws RuntimeException {
        if (repository == null)
            throw new RuntimeException("UserRepository must not be null!");
        //
        String token = getPropertyValue("token").toString();
        JWTHeader header = TokenValidator.parseHeader(token, JWTHeader.class);
        JWTPayload payload = TokenValidator.parsePayload(token, JWTPayload.class);
        Optional<User> exist = repository.findByUsername(payload.getIss());
        if (exist.isPresent()){
            String secret = getSecret(exist.get(), Integer.valueOf(header.getKid()));
            TokenProvider refresh = new JWTokenProvider(secret);
            Calendar timeToLive = getTimeToLive();
            String refreshToken = refresh.refreshToken(token, timeToLive);
            try {
                Map<String, String> data = new HashMap<>();
                data.put("X-Auth-Token", refreshToken);
                return new Response().setMessage(Message.marshal(data)).setStatus(200);
            } catch (IOException e) {
                return new Response().setMessage(e.getMessage()).setStatus(500);
            }
        }
        return new Response().setMessage("User doesn't exist!").setStatus(404);
    }

    @Override
    public Response abort(Response response) throws RuntimeException {
        String reason = response != null ? response.getPayload() : "UnknownError! @" + this.getClass().getSimpleName();
        return new Response().setMessage(reason).setStatus(500);
    }
}
