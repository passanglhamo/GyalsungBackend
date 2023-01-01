package com.microservice.erp.domain.tasks.iam;

import com.infoworks.lab.beans.tasks.nuts.AbstractTask;
import com.microservice.erp.domain.models.NewAccountRequest;
import com.microservice.erp.domain.models.NewTenantRequest;
import com.infoworks.lab.rest.models.Response;
import org.springframework.security.crypto.password.PasswordEncoder;

public class CreateNewTenant extends AbstractTask<Response, NewAccountRequest> {

    private Object repository;
    private PasswordEncoder encoder;

    public CreateNewTenant(PasswordEncoder encoder, Object repository, NewTenantRequest tenant) {
        super(tenant);
        this.encoder = encoder;
        this.repository = repository;
    }

    @Override
    public NewAccountRequest execute(Response message) throws RuntimeException {
        if (message != null && message.getStatus() != 200)
            return (NewAccountRequest) new NewAccountRequest().setStatus(500).setError("Internal Server Error!");
        //TODO:
        NewTenantRequest newTenantRequest = (NewTenantRequest) getMessage();
        //Check is already exist.
        //Do Create New Tenant Account using email and password.
        String securePass = encoder.encode(newTenantRequest.getPassword());
        //Save salted password into Persistence Layer.
        //..
        NewAccountRequest response = new NewAccountRequest();
        response.unmarshallingFromMap(newTenantRequest.marshallingToMap(true), true);
        //In-case of Tenant-user its always their email address will be their username:
        response.setUsername(newTenantRequest.getEmail());
        response.setStatus(200)
                .setMessage("NewTenant Created with Username: " + response.getUsername());
        return response;
    }

    @Override
    public NewAccountRequest abort(Response message) throws RuntimeException {
        String reason = message != null ? message.getPayload() : "UnknownError! @" + this.getClass().getSimpleName();
        return (NewAccountRequest) new NewAccountRequest().setMessage(reason).setStatus(500);
    }
}
