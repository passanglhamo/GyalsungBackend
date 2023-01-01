package com.microservice.erp.domain.tasks.am;

import com.infoworks.lab.beans.tasks.nuts.AbstractTask;
import com.infoworks.lab.rest.models.Message;
import com.infoworks.lab.rest.models.Response;
import com.it.soul.lab.sql.query.models.Property;

public class SaveTenant extends AbstractTask<Message, Response> {

    public SaveTenant(String data) {
        super(new Property("data", data));
    }

    @Override
    public Response execute(Message message) throws RuntimeException {
        String savedData = getPropertyValue("data").toString();
        //....
        //....
        return new Response().setMessage(savedData).setStatus(200);
    }

    @Override
    public Response abort(Message message) throws RuntimeException {
        String reason = message != null ? message.getPayload() : "UnknownError! @" + this.getClass().getSimpleName();
        return new Response().setMessage(reason).setStatus(500);
    }
}
