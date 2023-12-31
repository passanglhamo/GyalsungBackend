package com.microservice.erp.domain.tasks.am;

import com.infoworks.lab.beans.tasks.nuts.AbstractTask;
import com.microservice.erp.domain.entities.Policy;
import com.microservice.erp.domain.entities.User;
import com.microservice.erp.domain.repositories.UserRepository;
import com.infoworks.lab.rest.models.Message;
import com.infoworks.lab.rest.models.Response;
import com.it.soul.lab.sql.query.models.Property;

import java.util.Map;
import java.util.Optional;

public class RemovePolicyFromUser extends AbstractTask<Message, Response> {

    private UserRepository repository;
    private User user;
    private Policy policy;

    public RemovePolicyFromUser(UserRepository repository, User user, Policy policy) {
        super(new Property("user", user.marshallingToMap(true))
                , new Property("policy", policy.marshallingToMap(true)));
        this.repository = repository;
        this.user = user;
        this.policy = policy;
    }

    @Override
    public Response execute(Message message) throws RuntimeException {
        if (policy == null){
            Map<String, Object> savedData = (Map<String, Object>) getPropertyValue("policy");
            policy = new Policy();
            policy.unmarshallingFromMap(savedData, true);
        }
        if (user == null){
            Map<String, Object> savedData = (Map<String, Object>) getPropertyValue("user");
            user = new User();
            user.unmarshallingFromMap(savedData, true);
        }

        if (policy.getId() == null)
            return new Response().setMessage("Policy Not Found.").setStatus(500);

        if (repository != null){
            Optional<User> exist = repository.findByUserId(user.getUserId());
            if (exist.isPresent()){
                user = exist.get();
                user.getPolicies().remove(policy);
                User saved = repository.save(user);
                return new Response().setMessage("User successfully Updated with policy removed: " + saved.getId()).setStatus(200);
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
