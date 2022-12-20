//package com.microservice.erp.domain.tasks.iam;
//
//import com.fasterxml.jackson.core.type.TypeReference;
//import com.microservice.erp.domain.models.LoginRequest;
//import com.microservice.erp.domain.repositories.UserRepository;
//import com.infoworks.lab.jwtoken.definition.TokenProvider;
//import com.infoworks.lab.rest.models.Message;
//import com.infoworks.lab.rest.models.Response;
//import com.it.soul.lab.sql.query.models.Property;
//import org.springframework.security.crypto.password.PasswordEncoder;
//
//import java.io.IOException;
//import java.time.Duration;
//import java.util.Calendar;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.Optional;
//
//public class Login extends TokenizerTask {
//
//    private UserRepository repository;
//    private PasswordEncoder encoder;
//
//    public Login(UserRepository repository, PasswordEncoder encoder, Property... properties) {
//        super(properties);
//        this.repository = repository;
//        this.encoder = encoder;
//    }
//
//    public Login(UserRepository repository, PasswordEncoder encoder, String username){
//        this(repository, encoder, new Property("username", username));
//    }
//
//    public Login(UserRepository repository, PasswordEncoder encoder, LoginRequest request) {
//        this(repository, encoder, request.getRow().getProperties().toArray(new Property[0]));
//    }
//
//    @Override
//    public Response getMessage() {
//        try {
//            Message saved = super.getMessage();
//            Map<String, Object> data = Message.unmarshal(new TypeReference<Map<String, Object>>() {}, saved.getPayload());
//            saved = new LoginRequest();
//            saved.unmarshallingFromMap(data, true);
//            return (Response) saved;
//        } catch (IOException e) {}
//        return super.getMessage();
//    }
//
//    @Override
//    protected Calendar getTimeToLive() {
//        LoginRequest request = (LoginRequest) getMessage();
//        Calendar cal = Calendar.getInstance();
//        Duration ttl = Duration.ofMillis(request.getTokenTtl());
//        if (ttl.isZero() || ttl.isNegative())
//            return TokenProvider.defaultTokenTimeToLive();
//        //Override the default:
//        cal.add(Calendar.MILLISECOND, Long.valueOf(ttl.toMillis()).intValue());
//        return cal;
//    }
//
//    //@Override
//    public Response execute(Response message) throws RuntimeException {
//        if (message != null && message.getStatus() != 200)
//            return message;
//        if (!(getMessage() instanceof LoginRequest))
//            return new Response().setStatus(500).setError("Internal Server Error!");
//        if (repository == null)
//            throw new RuntimeException("UserRepository must not be null!");
//        //Do the login:
//        LoginRequest request = (LoginRequest) getMessage();
//        Optional<User> exist = repository.findByUsername(request.getUsername());
//        if (exist.isPresent()){
//            //PasswordEncoder::matches(RawPassword, EncodedPassword) == will return true/false
//            if (!encoder.matches(request.getPassword(), exist.get().getPassword()))
//                return new Response().setStatus(401).setMessage("Password didn't matched.");
//
//            //Now existing password matched: lets create the JWT token;
//            try {
//                //We create a token for 1 Hour:
//                Calendar timeToLive = getTimeToLive();
//                String tokenKey = getToken(exist.get(), timeToLive);
//                Map<String, String> data = new HashMap<>();
//                data.put("X-Auth-Token", tokenKey);
//                data.put("userId", exist.get().getId().toString());
//                data.put("roles", exist.get().getRoles().toString());
//                return new Response().setStatus(200).setMessage(Message.marshal(data));
//            } catch (IOException e) {
//                return new Response().setStatus(500).setMessage(e.getMessage());
//            }
//        } else {
//            return new Response().setStatus(404).setMessage("User doesn't exist: " + request.getUsername());
//        }
//    }
//
//    @Override
//    public Response abort(Response message) throws RuntimeException {
//        String reason = message != null ? message.getPayload() : "UnknownError! @" + this.getClass().getSimpleName();
//        return new Response().setMessage(reason).setStatus(500);
//    }
//
//}
