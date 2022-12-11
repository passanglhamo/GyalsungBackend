package com.microservice.erp.domain.beans.tasks;

import com.infoworks.lab.beans.tasks.nuts.AbstractTask;
import com.microservice.erp.domain.beans.models.Push;
import com.infoworks.lab.rest.models.Response;

public class SendPush extends AbstractTask<Push, Response> {

    public SendPush() {}

    public SendPush(Push message) {
        super(message);
    }

    @Override
    public Response execute(Push sms) throws RuntimeException {
        if (sms == null){
            sms = getMessage();
        }
        return new Response().setStatus(200).setMessage("Push Sent");
    }

    @Override
    public Response abort(Push sms) throws RuntimeException {
        return new Response().setStatus(500).setError("Exception In SendPush");
    }
}
