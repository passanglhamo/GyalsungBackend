package com.microservice.erp.task.iam;

import com.infoworks.lab.beans.tasks.nuts.AbstractTask;
import com.infoworks.lab.rest.models.Response;
import com.it.soul.lab.sql.query.models.Property;

public class MakeTokenExpired extends AbstractTask<Response, Response> {

    public MakeTokenExpired(String token) {
        super(new Property("token", token));
    }

    @Override
    public Response execute(Response message) throws RuntimeException {
        if (message != null && message.getStatus() != 200)
            return message;
        //TODO: DO THE BUSINESS LOGIC TO MakeTokenExpired:
        String token = getPropertyValue("token").toString();
        //....
        System.out.println("MakeTokenExpired is Successful for " + token);
        //....
        return new Response().setMessage("").setStatus(200);
    }

    @Override
    public Response abort(Response message) throws RuntimeException {
        String reason = message != null ? message.getPayload() : "UnknownError! @" + this.getClass().getSimpleName();
        return new Response().setMessage(reason).setStatus(500);
    }
}
