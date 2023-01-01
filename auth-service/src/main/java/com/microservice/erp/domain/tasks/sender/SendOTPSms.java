package com.microservice.erp.domain.tasks.sender;

import com.infoworks.lab.rest.models.Message;
import com.infoworks.lab.rest.models.Response;

public class SendOTPSms extends SendSMS {

    public SendOTPSms(String sender, String receiver, String subject, String template) {
        super(sender, receiver, subject, template);
    }

    @Override
    public Response execute(Message message) throws RuntimeException {
        //TODO: DO THE BUSINESS LOGIC TO SEND OTP SMS:
        String sender = getPropertyValue("sender").toString();
        String receiver = getPropertyValue("receiver").toString();
        String subject = getPropertyValue("subject").toString();
        String template = getPropertyValue("template").toString();
        //....
        System.out.println("OTP Has Sent To " + receiver);
        //....
        return new Response().setMessage("").setStatus(200);
    }

    @Override
    public Response abort(Message message) throws RuntimeException {
        String reason = message != null ? message.getPayload() : "UnknownError!";
        return new Response().setMessage(reason).setStatus(500);
    }
}
