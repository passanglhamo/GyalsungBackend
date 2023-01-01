package com.microservice.erp.domain.tasks.am;

import com.infoworks.lab.beans.tasks.nuts.AbstractTask;
import com.microservice.erp.domain.entities.Policy;
import com.microservice.erp.domain.entities.User;
import com.microservice.erp.domain.repositories.PolicyRepository;
import com.microservice.erp.domain.repositories.UserRepository;
import com.infoworks.lab.rest.models.Message;
import com.infoworks.lab.rest.models.Response;
import com.it.soul.lab.sql.query.models.Property;

import java.util.Map;
import java.util.Optional;

public class AssignPolicyToUser extends AbstractTask<Message, Response> {

    private UserRepository userRepository;
    private PolicyRepository policyRepository;
    private User user;
    private Policy policy;

    public AssignPolicyToUser(UserRepository userRepository, PolicyRepository policyRepository, User user, Policy policy) {
        super(new Property("user", user.marshallingToMap(true))
        , new Property("policy", policy.marshallingToMap(true)));
        this.userRepository = userRepository;
        this.policyRepository = policyRepository;
        this.user = user;
        this.policy = policy;
    }

    @Override
    public Response execute(Message message) throws RuntimeException {
        if (user == null){
            Map<String, Object> savedData = (Map<String, Object>) getPropertyValue("user");
            user = new User();
            user.unmarshallingFromMap(savedData, true);
        }
        if (policy == null){
            Map<String, Object> savedData = (Map<String, Object>) getPropertyValue("policy");
            policy = new Policy();
            policy.unmarshallingFromMap(savedData, true);
        }
        if (userRepository != null){
            Optional<User> existingUser = userRepository.findByUsername(user.getUsername());
            if(!existingUser.isPresent()) return new Response().setMessage("User Not Found.").setStatus(500);

            Optional<Policy> existingPolicy = policyRepository.findByServiceName(policy.getServiceName());
            if(!existingPolicy.isPresent()) return new Response().setMessage("Policy Not Found.").setStatus(500);

            user = existingUser.get();
            user.addPolicies(existingPolicy.get());
            User saved = userRepository.save(user);
            return new Response().setMessage("Successfully Policy Assigned with User: " + saved.getId()).setStatus(200);
        }
        return new Response().setMessage("UserRepository is null.").setStatus(500);
    }

    @Override
    public Response abort(Message message) throws RuntimeException {
        String reason = message != null ? message.getPayload() : "UnknownError! @" + this.getClass().getSimpleName();
        return new Response().setMessage(reason).setStatus(500);
    }
}
