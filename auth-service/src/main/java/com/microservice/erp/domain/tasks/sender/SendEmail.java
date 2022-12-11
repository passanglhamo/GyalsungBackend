package com.microservice.erp.domain.tasks.sender;

import com.fasterxml.jackson.core.type.TypeReference;
import com.infoworks.lab.beans.tasks.nuts.AbstractTask;
import com.infoworks.lab.rest.models.Message;
import com.infoworks.lab.rest.models.Response;
import com.it.soul.lab.sql.query.models.Property;
import com.it.soul.lab.sql.query.models.Row;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.client.RootUriTemplateHandler;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SendEmail extends AbstractTask<Message, Response> {

    private static Logger LOG = LoggerFactory.getLogger(SendEmail.class);
    private RestTemplate notifyTemplate;

    public SendEmail(String sender, String receiver, String subject, String template, Row properties) {
        this(new RestTemplate(), sender, receiver, subject, template, properties);
    }

    public SendEmail(RestTemplate notifyTemplate, String sender, String receiver, String subject, String template, Row properties) {
        super(new Property("sender", sender)
                , new Property("receiver", receiver)
                , new Property("subject", subject)
                , new Property("template",template)
                , new Property("properties", properties.keyObjectMap()));
        this.notifyTemplate = notifyTemplate;
    }

    public void setNotifyTemplate(RestTemplate notifyTemplate) {
        this.notifyTemplate = notifyTemplate;
    }

    protected RestTemplate getNotifyTemplate() {
        return notifyTemplate;
    }

    @Override
    public Response execute(Message message) throws RuntimeException {
        if (message != null && message instanceof Response){
            if (((Response) message).getStatus() >= 400)
                return (Response) message;
        }
        //...
        String sender = getPropertyValue("sender").toString();
        String receiver = getPropertyValue("receiver").toString();
        String subject = getPropertyValue("subject").toString();
        String emailTemplateID = getPropertyValue("template").toString();
        Object properties = getPropertyValue("properties");
        //...
        HttpHeaders httpHeaders = new HttpHeaders();
        Map<String, Object> body = new HashMap<>();
        body.put("from", sender);
        body.put("to", receiver);
        body.put("subject", subject);
        body.put("template", emailTemplateID);
        body.put("properties", properties);
        HttpEntity<Map> httpEntity = new HttpEntity<>(body, httpHeaders);
        //"http://<domain:port>/api/notify/v1/mail"
        String rootUri = ((RootUriTemplateHandler)getNotifyTemplate()
                .getUriTemplateHandler())
                .getRootUri();
        try {
            ResponseEntity<String> response = getNotifyTemplate().exchange(rootUri
                    , HttpMethod.POST
                    , httpEntity
                    , String.class);
            message = new Response().setStatus(response.getStatusCodeValue())
                    .setMessage(response.getBody());
        } catch (HttpClientErrorException e) {
            LOG.error(e.getMessage());
        }
        //Finally:
        return (message != null && message instanceof Response)
                ? (Response) message
                : new Response().setStatus(500)
                .setMessage("Error@" + getClass().getSimpleName() + "Message is Null or Not a Response type.");
    }

    protected void updateProperty(Property property) {
        String payload = getMessage().getPayload();
        if (Message.isValidJson(payload)){
            if (payload.startsWith("[")) { throw new RuntimeException("AbstractTask: JsonArray is not supported."); }
            try {
                Map<String, Object> old = Message.unmarshal(new TypeReference<Map<String, Object>>() {}, payload);
                Map<String, Object> properties = (Map<String, Object>) old.get("properties");
                if(properties != null) properties.put(property.getKey(), property.getValue());
                payload = Message.marshal(old);
                getMessage().setPayload(payload);
                return;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        throw new RuntimeException("AbstractTask: Invalid Property Access");
    }

    @Override
    public Response abort(Message message) throws RuntimeException {
        String reason = message != null ? message.getPayload() : "UnknownError!";
        return new Response().setMessage(reason).setStatus(500);
    }
}
