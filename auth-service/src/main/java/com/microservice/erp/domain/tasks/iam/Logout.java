package com.microservice.erp.domain.tasks.iam;

import com.infoworks.lab.beans.tasks.nuts.AbstractTask;
import com.infoworks.lab.rest.models.Response;
import com.it.soul.lab.sql.query.models.Property;

public class Logout extends AbstractTask<Response, Response> {

    public Logout(String token) {
        super(new Property("token", token));
    }

    @Override
    public Response execute(Response message) throws RuntimeException {
        if (message != null && message.getStatus() != 200)
            return message;
        //TODO: DO THE BUSINESS LOGIC TO LOGOUT:
        String token = getPropertyValue("token").toString();
        //....
        System.out.println("Logout is Successful for " + token);
        //....
        return new Response().setMessage("").setStatus(200);
    }

    @Override
    public Response abort(Response message) throws RuntimeException {
        String reason = message != null ? message.getPayload() : "UnknownError! @" + this.getClass().getSimpleName();
        return new Response().setMessage(reason).setStatus(500);
    }
}
