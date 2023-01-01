package com.microservice.erp.domain.tasks.am;

import com.infoworks.lab.beans.tasks.nuts.AbstractTask;
import com.microservice.erp.domain.entities.Role;
import com.microservice.erp.domain.entities.User;
import com.microservice.erp.domain.repositories.UserRepository;
import com.infoworks.lab.rest.models.Message;
import com.infoworks.lab.rest.models.Response;
import com.it.soul.lab.sql.query.models.Property;

import java.util.Map;
import java.util.Optional;

public class RemoveRoleFromUser extends AbstractTask<Message, Response> {

    private UserRepository repository;
    private User user;
    private Role role;

    public RemoveRoleFromUser(UserRepository repository, User user, Role role) {
        super(new Property("user", user.marshallingToMap(true))
                , new Property("role", role.marshallingToMap(true)));
        this.repository = repository;
        this.user = user;
        this.role = role;
    }

    @Override
    public Response execute(Message message) throws RuntimeException {
        if (role == null){
            Map<String, Object> savedData = (Map<String, Object>) getPropertyValue("role");
            role = new Role();
            role.unmarshallingFromMap(savedData, true);
        }
        if (user == null){
            Map<String, Object> savedData = (Map<String, Object>) getPropertyValue("user");
            user = new User();
            user.unmarshallingFromMap(savedData, true);
        }

        if (role.getId() == null)
            return new Response().setMessage("Role Not Found.").setStatus(500);

        if (repository != null){
            Optional<User> exist = repository.findByUsername(user.getUsername());
            if (exist.isPresent()){
                user = exist.get();
                user.getRoles().remove(role);
                User saved = repository.save(user);
                return new Response().setMessage("User successfully Updated with role removed: " + saved.getId()).setStatus(200);
            }
            return new Response().setMessage("User Not Found.").setStatus(500);
        }
        return new Response().setMessage("UserRepository is null.").setStatus(500);
    }

    @Override
    public Response abort(Message message) throws RuntimeException {
        String reason = message != null ? message.getPayload() : "UnknownError! @" + this.getClass().getSimpleName();
        return new Response().setMessage(reason).setStatus(500);
    }
}
