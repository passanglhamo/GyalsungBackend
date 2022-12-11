package com.microservice.erp.domain.tasks.iam;

import com.microservice.erp.domain.entities.User;
import com.microservice.erp.domain.repositories.UserRepository;
import com.infoworks.lab.jjwt.JWTHeader;
import com.infoworks.lab.jjwt.JWTPayload;
import com.infoworks.lab.jjwt.JWTValidator;
import com.infoworks.lab.jjwt.TokenValidator;
import com.infoworks.lab.rest.models.Message;
import com.infoworks.lab.rest.models.Response;
import com.it.soul.lab.sql.query.models.Property;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ChangePassword extends TokenizerTask {

    private UserRepository repository;
    private PasswordEncoder encoder;

    public ChangePassword(String token, String oldPass, String newPass, UserRepository repository, PasswordEncoder encoder) {
        super(new Property("token", token), new Property("oldPass", oldPass), new Property("newPass", newPass));
        this.repository = repository;
        this.encoder = encoder;
    }

    @Override
    public Response execute(Response response) throws RuntimeException {
        if (repository == null)
            throw new RuntimeException("UserRepository must not be null!");
        if (encoder == null)
            throw new RuntimeException("PasswordEncoder must not be null!");
        //
        String token = getPropertyValue("token").toString();
        String oldPass = getPropertyValue("oldPass").toString();
        String newPass = getPropertyValue("newPass").toString();
        //
        JWTHeader header = TokenValidator.parseHeader(token, JWTHeader.class);
        JWTPayload payload = TokenValidator.parsePayload(token, JWTPayload.class);
        Optional<User> exist = repository.findByUsername(payload.getIss());
        if (exist.isPresent()){
            String secret = getSecret(exist.get(), Integer.valueOf(header.getKid()));
            //
            boolean isValid = new JWTValidator().isValid(token, secret);
            boolean oldPassMatch = encoder.matches(oldPass, exist.get().getPassword());
            String msg = isValid ? "Invalid Token" : "Old-Password didn't matched";
            //
            if (isValid && oldPassMatch){
                User user = exist.get();
                user.setPassword(encoder.encode(newPass));
                User updated = repository.save(user);
                if (updated != null){
                    try {
                        //We create a token for 1 Hour:
                        Calendar timeToLive = getTimeToLive();
                        String tokenKey = getToken(updated, timeToLive);
                        Map<String, String> data = new HashMap<>();
                        data.put("X-Auth-Token", tokenKey);
                        return new Response().setStatus(200).setMessage(Message.marshal(data));
                    } catch (IOException e) {
                        return new Response().setStatus(500).setMessage(e.getMessage());
                    }
                } else {
                    msg = "Password update failed";
                }
            }
            return new Response().setMessage(msg).setStatus(401);
        }
        return new Response().setMessage("User doesn't exist!").setStatus(404);
    }

    @Override
    public Response abort(Response response) throws RuntimeException {
        String reason = response != null ? response.getPayload() : "UnknownError! @" + this.getClass().getSimpleName();
        return new Response().setMessage(reason).setStatus(500);
    }
}
