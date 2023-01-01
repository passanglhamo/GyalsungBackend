package com.microservice.erp.domain.tasks.sender;

import com.fasterxml.jackson.core.type.TypeReference;
import com.infoworks.lab.rest.models.Message;
import com.infoworks.lab.rest.models.Response;
import com.it.soul.lab.sql.query.models.Property;
import com.it.soul.lab.sql.query.models.Row;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Map;

public class SendForgetPasswordEmail extends SendEmail {

    private static Logger LOG = LoggerFactory.getLogger(SendForgetPasswordEmail.class);

    public SendForgetPasswordEmail(String sender, String receiver, String subject, String template, Row properties) {
        super(sender, receiver, subject, template, properties);
    }

    public SendForgetPasswordEmail(RestTemplate notifyTemplate, String sender, String receiver, String subject, String template, Row properties) {
        super(notifyTemplate, sender, receiver, subject, template, properties);
    }

    @Override
    public Response execute(Message message) throws RuntimeException {
        if (message != null && message instanceof Response){
            try {
                Map<String, Object> pro = (Map<String, Object>) getPropertyValue("properties");
                String href = pro.get("resetRef").toString();
                Response msg = (Response) message;
                Map<String, String> res = Message.unmarshal(new TypeReference<Map<String, String>>() {}, msg.getMessage());
                String resetToken = res.get("Reset-Pass-Token");
                href = href + resetToken;
                updateProperty(new Property("resetRef", href));
                super.execute(message);
            } catch (IOException e) {
                LOG.error(e.getMessage());
            }
            //Finally:
            return (Response) message;
        }
        return new Response().setStatus(500)
                .setError("Error@" + getClass().getSimpleName() + "Message is Null or Not a Response type.");
    }
}
