//package com.microservice.erp.domain.tasks.iam;
//
//import com.microservice.erp.domain.entities.User;
//import com.microservice.erp.domain.repositories.UserRepository;
//import com.infoworks.lab.rest.models.Message;
//import com.infoworks.lab.rest.models.Response;
//import com.it.soul.lab.sql.query.models.Property;
//
//import java.io.IOException;
//import java.time.Duration;
//import java.util.Calendar;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.Optional;
//
//public class ForgotPassword extends TokenizerTask {
//
//    private UserRepository repository;
//
//    public ForgotPassword(UserRepository repository, String username) {
//        this(repository, username, Duration.ofHours(1));
//    }
//
//    public ForgotPassword(UserRepository repository, String username, Duration ttl) {
//        super(new Property("username", username), new Property("ttl", ttl.toMillis()));
//        this.repository = repository;
//    }
//
//    @Override
//    public Response execute(Response response) throws RuntimeException {
//        if (repository == null)
//            throw new RuntimeException("UserRepository must not be null!");
//        //
//        String username = getPropertyValue("username").toString();
//        Optional<User> exist = repository.findByUsername(username);
//        if (exist.isPresent()){
//            //We create a token for 1 hour:
//            Calendar timeToLive = getTimeToLive();
//            String tokenKey = getToken(exist.get(), timeToLive);
//            try {
//                Map<String, String> data = new HashMap<>();
//                data.put("Reset-Pass-Token", tokenKey);
//                return new Response().setMessage(Message.marshal(data)).setStatus(200);
//            } catch (IOException e) {
//                return new Response().setMessage(e.getMessage()).setStatus(500);
//            }
//        }
//        return new Response().setMessage("User doesn't exist. ForgotPassword failed.").setStatus(404);
//    }
//
//    @Override
//    public Response abort(Response response) throws RuntimeException {
//        String reason = response != null ? response.getPayload() : "UnknownError! @" + this.getClass().getSimpleName();
//        return new Response().setMessage(reason).setStatus(500);
//    }
//}
