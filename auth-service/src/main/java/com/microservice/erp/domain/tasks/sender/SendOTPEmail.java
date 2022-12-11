package com.microservice.erp.domain.tasks.sender;

import com.infoworks.lab.rest.models.Message;
import com.infoworks.lab.rest.models.Response;
import com.it.soul.lab.sql.query.models.Property;
import com.it.soul.lab.sql.query.models.Row;
import org.springframework.boot.web.client.RootUriTemplateHandler;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

public class SendOTPEmail extends SendEmail {

    public SendOTPEmail(String sender, String receiver, String subject, String template, Row properties) {
        super(sender, receiver, subject, template, properties);
    }

    public SendOTPEmail(RestTemplate notifyTemplate, String sender, String receiver, String subject, String template, Row properties) {
        super(notifyTemplate, sender, receiver, subject, template, properties);
    }

    @Override
    public Response execute(Message message) throws RuntimeException {
        if (message != null && message instanceof Response){
            if (((Response) message).getStatus() >= 400)
                return (Response) message;
        }
        //...
        String receiver = getPropertyValue("receiver").toString();
        HttpHeaders httpHeaders = new HttpHeaders();
        Map<String, Object> body = new HashMap<>();
        HttpEntity<Map> httpEntity = new HttpEntity<>(body, httpHeaders);
        //"http://<domain:port>/api/notify/v1/otp/{username}"
        String rootUri = ((RootUriTemplateHandler)getNotifyTemplate()
                .getUriTemplateHandler())
                .getRootUri();
        ResponseEntity<Map> response = getNotifyTemplate().exchange(rootUri
                , HttpMethod.POST
                , httpEntity
                , Map.class
                , receiver);
        String otp = response.getBody().get("value").toString();
        updateProperty(new Property("otp", otp));
        return super.execute(message);
    }

    @Override
    public Response abort(Message message) throws RuntimeException {
        String reason = message != null ? message.getPayload() : "UnknownError!";
        return new Response().setMessage(reason).setStatus(500);
    }
}
