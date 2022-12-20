//package com.microservice.erp.domain.tasks.am;
//
//import com.infoworks.lab.beans.tasks.nuts.AbstractTask;
//import com.microservice.erp.domain.entities.User;
//import com.microservice.erp.domain.repositories.UserRepository;
//import com.infoworks.lab.rest.models.Message;
//import com.infoworks.lab.rest.models.Response;
//import com.it.soul.lab.sql.query.models.Property;
//
//import java.util.Map;
//import java.util.Optional;
//
//public class SaveUser extends AbstractTask<Message, Response> {
//
//    private UserRepository repository;
//    private User user;
//
//    public SaveUser(UserRepository repository, User data) {
//        super(new Property("user", data.marshallingToMap(true)));
//        this.repository = repository;
//        this.user = data;
//    }
//
//    @Override
//    public Response execute(Message message) throws RuntimeException {
//        if (user == null){
//            Map<String, Object> savedData = (Map<String, Object>) getPropertyValue("user");
//            user = new User();
//            user.unmarshallingFromMap(savedData, true);
//        }
//        if (repository != null){
//            Optional<User> exist = repository.findByUsername(user.getUsername());
//            Response response = new Response().setStatus(200);
//            if (!exist.isPresent()){
//                User saved = repository.save(user);
//                response.setMessage("User successfully created: " + saved.getId());
//            }else {
//                user.setId(exist.get().getId());
//                user.setUsername(exist.get().getUsername());
//                user.setEmail(exist.get().getEmail());
//                user.setMobile(exist.get().getMobile());
//                User saved = repository.save(user);
//                response.setMessage("User successfully updated: " + saved.getId());
//            }
//            return response;
//        }
//        return new Response().setMessage("UserRepository is null.").setStatus(500);
//    }
//
//    @Override
//    public Response abort(Message message) throws RuntimeException {
//        String reason = message != null ? message.getPayload() : "UnknownError! @" + this.getClass().getSimpleName();
//        return new Response().setMessage(reason).setStatus(500);
//    }
//}
