package com.microservice.erp.domain.tasks.am;

import com.infoworks.lab.beans.tasks.nuts.AbstractTask;
import com.microservice.erp.domain.entities.Role;
import com.microservice.erp.domain.repositories.RoleRepository;
import com.infoworks.lab.rest.models.Message;
import com.infoworks.lab.rest.models.Response;
import com.it.soul.lab.sql.query.models.Property;

import java.util.Map;
import java.util.Optional;

public class SaveRole extends AbstractTask<Message, Response> {

    private RoleRepository repository;
    private Role role;

    public SaveRole(RoleRepository repository, Role data) {
        super(new Property("role", data.marshallingToMap(true)));
        this.repository = repository;
        this.role = data;
    }

    @Override
    public Response execute(Message message) throws RuntimeException {
        if (role == null){
            Map<String, Object> savedData = (Map<String, Object>) getPropertyValue("role");
            role = new Role();
            role.unmarshallingFromMap(savedData, true);
        }
        if (repository != null){
            Optional<Role> exist = repository.findRoleByRoleName(role.getRoleName());
            if (!exist.isPresent()){
                Role saved = repository.save(role);
                return new Response().setMessage("Role successfully created: " + saved.getId()).setStatus(200);
            } else {
                role.setId(exist.get().getId());
                Role saved = repository.save(role);
                return new Response().setMessage("Role successfully updated: " + saved.getId()).setStatus(200);
            }
        }
        return new Response().setMessage("RoleRepository is null.").setStatus(500);
    }

    @Override
    public Response abort(Message message) throws RuntimeException {
        String reason = message != null ? message.getPayload() : "UnknownError! @" + this.getClass().getSimpleName();
        return new Response().setMessage(reason).setStatus(500);
    }
}
