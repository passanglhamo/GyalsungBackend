package com.microservice.erp.domain.tasks.sender;

import com.infoworks.lab.beans.tasks.nuts.AbstractTask;
import com.infoworks.lab.rest.models.Message;
import com.infoworks.lab.rest.models.Response;
import com.it.soul.lab.sql.query.models.Property;

public class SendSMS extends AbstractTask<Message, Response> {

    public SendSMS(String sender, String receiver, String subject, String template) {
        super(new Property("sender", sender)
                , new Property("receiver", receiver)
                , new Property("subject", subject)
                , new Property("template",template));
    }

    @Override
    public Response execute(Message message) throws RuntimeException {
        //TODO: DO THE BUSINESS LOGIC TO SEND SMS:
        String sender = getPropertyValue("sender").toString();
        String receiver = getPropertyValue("receiver").toString();
        String subject = getPropertyValue("subject").toString();
        String template = getPropertyValue("template").toString();
        //....
        System.out.println("SMS Has Sent To " + receiver);
        //....
        return new Response().setMessage("").setStatus(200);
    }

    @Override
    public Response abort(Message message) throws RuntimeException {
        String reason = message != null ? message.getPayload() : "UnknownError!";
        return new Response().setMessage(reason).setStatus(500);
    }
}
