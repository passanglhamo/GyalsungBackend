package com.microservice.erp.domain.tasks.am;

import com.infoworks.lab.beans.tasks.nuts.AbstractTask;
import com.microservice.erp.domain.entities.Policy;
import com.infoworks.lab.rest.models.Message;
import com.infoworks.lab.rest.models.Response;
import com.it.soul.lab.sql.query.models.Property;
import com.microservice.erp.domain.repositories.IPolicyRepository;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class SavePolicy extends AbstractTask<Message, Response> {

    private IPolicyRepository repository;
    private Policy policy;

    public SavePolicy(IPolicyRepository repository, Policy data) {
        super(new Property("policy", data.marshallingToMap(true)));
        this.repository = repository;
        this.policy = data;
    }

    @Override
    public Response execute(Message message) throws RuntimeException {
        if (policy == null){
            Map<String, Object> savedData = (Map<String, Object>) getPropertyValue("policy");
            policy = new Policy();
            policy.unmarshallingFromMap(savedData, true);
        }
        if (repository != null){
            Optional<Policy> exist = Objects.isNull(policy.getId())?Optional.empty():repository.findById(policy.getId());
//            Optional<Policy> exist = repository.findByPolicyName(policy.getPolicyName());
            if (!exist.isPresent()){
                Policy saved = repository.save(policy);
                return new Response().setMessage("Policy successfully created for: " + policy.getPolicyName()).setStatus(200);
            } else {
                policy.setId(exist.get().getId());
                Policy saved = repository.save(policy);
                return new Response().setMessage("Policy successfully updated for: " +  policy.getPolicyName()).setStatus(200);
            }
        }
        return new Response().setMessage("PolicyRepository is null.").setStatus(500);
    }

    @Override
    public Response abort(Message message) throws RuntimeException {
        String reason = message != null ? message.getPayload() : "UnknownError! @" + this.getClass().getSimpleName();
        return new Response().setMessage(reason).setStatus(500);
    }
}
