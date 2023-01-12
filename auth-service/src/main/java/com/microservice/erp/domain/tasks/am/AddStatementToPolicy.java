package com.microservice.erp.domain.tasks.am;

import com.infoworks.lab.beans.tasks.nuts.AbstractTask;
import com.microservice.erp.domain.entities.Policy;
import com.microservice.erp.domain.entities.Statement;
import com.infoworks.lab.rest.models.Message;
import com.infoworks.lab.rest.models.Response;
import com.it.soul.lab.sql.query.models.Property;
import com.microservice.erp.domain.repositories.IPolicyRepository;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class AddStatementToPolicy extends AbstractTask<Message, Response> {

    private IPolicyRepository repository;
    private Policy policy;
    private Statement[] statements;

    public AddStatementToPolicy(IPolicyRepository repository, Policy policy, Statement...statements) {
        super(new Property("policy", policy.marshallingToMap(true)));
        this.repository = repository;
        this.policy = policy;
        this.statements = statements;
    }

    @Override
    public Response execute(Message message) throws RuntimeException {
        if (policy == null){
            Map<String, Object> savedData = (Map<String, Object>) getPropertyValue("policy");
            policy = new Policy();
            policy.unmarshallingFromMap(savedData, true);
        }
        if (repository != null){
            Optional<Policy> exist = repository.findById(policy.getId());
            if (exist.isPresent()){
                policy = exist.get();
                policy.addStatements(statements);
                Policy saved = repository.save(policy);
                return new Response().setMessage("Policy successfully Updated with Statements: " + saved.getId()).setStatus(200);
            }
            return new Response().setMessage("Policy Not Found.").setStatus(500);
        }
        return new Response().setMessage("PolicyRepository is null.").setStatus(500);
    }

    @Override
    public Response abort(Message message) throws RuntimeException {
        String reason = message != null ? message.getPayload() : "UnknownError! @" + this.getClass().getSimpleName();
        return new Response().setMessage(reason).setStatus(500);
    }
}
