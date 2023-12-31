package com.microservice.erp.domain.tasks.iam;

import com.microservice.erp.domain.entities.User;
import com.microservice.erp.domain.repositories.UserRepository;
import com.infoworks.lab.jjwt.JWTHeader;
import com.infoworks.lab.jjwt.JWTPayload;
import com.infoworks.lab.jjwt.JWTValidator;
import com.infoworks.lab.jjwt.TokenValidator;
import com.infoworks.lab.rest.models.Response;
import com.it.soul.lab.sql.query.models.Property;

import java.util.Optional;

public class CheckTokenValidity extends TokenizerTask {

    private UserRepository repository;

    public CheckTokenValidity(String token, UserRepository repository) {
        super(new Property("token", token));
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
            try {
                boolean isValid = new JWTValidator().isValid(token, secret);
                return (isValid)
                        ? new Response().setMessage("Token is Valid").setStatus(200)
                        : new Response().setMessage("Token is Invalid").setStatus(401);
            } catch (Exception e) {
                return new Response().setMessage("Token is Invalid").setStatus(401);
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
