package com.microservice.erp.domain.tasks.am;

import com.infoworks.lab.beans.tasks.nuts.AbstractTask;
import com.microservice.erp.domain.entities.Policy;
import com.microservice.erp.domain.entities.Role;
import com.microservice.erp.domain.repositories.IPolicyRepository;
import com.microservice.erp.domain.repositories.IRoleRepository;
import com.infoworks.lab.rest.models.Message;
import com.infoworks.lab.rest.models.Response;
import com.it.soul.lab.sql.query.models.Property;

import java.util.Map;
import java.util.Optional;

public class AssignPolicyToRole extends AbstractTask<Message, Response> {

    private IRoleRepository repository;
    private IPolicyRepository policyRepository;
    private Role role;
    private Policy policy;

    public AssignPolicyToRole(IRoleRepository repository, IPolicyRepository policyRepository, Role role, Policy policy) {
        super(new Property("role", role.marshallingToMap(true))
                , new Property("policy", policy.marshallingToMap(true)));
        this.repository = repository;
        this.policyRepository = policyRepository;
        this.role = role;
        this.policy = policy;
    }

    @Override
    public Response execute(Message message) throws RuntimeException {
        if (policy == null){
            Map<String, Object> savedData = (Map<String, Object>) getPropertyValue("policy");
            policy = new Policy();
            policy.unmarshallingFromMap(savedData, true);
        }
        if (role == null){
            Map<String, Object> savedData = (Map<String, Object>) getPropertyValue("role");
            role = new Role();
            role.unmarshallingFromMap(savedData, true);
        }
        if (repository != null){
            Optional<Role> existingRole = repository.findById(role.getId());
            if(!existingRole.isPresent()) return new Response().setMessage("Role Not Found.").setStatus(500);

            Optional<Policy> existingPolicy = policyRepository.findById(policy.getId());
            if(!existingPolicy.isPresent()) return new Response().setMessage("Policy Not Found.").setStatus(500);
            role = existingRole.get();

            boolean roleExists = existingPolicy.get().getRoles().stream()
                    .anyMatch(r -> r.getId().equals(role.getId()));
            if(roleExists){
                return new Response().setMessage(existingPolicy.get().getPolicyName()+" and "+ role.getRoleName()+ " is already mapped.").setStatus(208);
            }
            role.addPolicies(existingPolicy.get());
            Role saved = repository.save(role);
            return new Response().setMessage("Successfully Policy Assigned with Role : " + role.getRoleName()).setStatus(200);
        }
        return new Response().setMessage("RoleRepository is null.").setStatus(500);
    }

    @Override
    public Response abort(Message message) throws RuntimeException {
        String reason = message != null ? message.getPayload() : "UnknownError! @" + this.getClass().getSimpleName();
        return new Response().setMessage(reason).setStatus(500);
    }
}
