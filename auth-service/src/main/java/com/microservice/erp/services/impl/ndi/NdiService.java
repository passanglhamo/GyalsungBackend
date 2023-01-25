package com.microservice.erp.services.impl.ndi;

import com.fasterxml.jackson.core.type.TypeReference;
import com.infoworks.lab.rest.models.Message;
import com.microservice.erp.domain.dto.MessageResponse;
import com.microservice.erp.domain.entities.User;
import com.microservice.erp.domain.models.LoginRequest;
import com.microservice.erp.domain.repositories.UserRepository;
import com.microservice.erp.services.definition.iLogin;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import io.nats.client.Connection;
import io.nats.client.Dispatcher;
import io.nats.client.Nats;
import io.nats.client.Options;

@Component
public class NdiService {
    private UserRepository userRepository;
    private iLogin login;


    public NdiService(UserRepository userRepository, iLogin login) {
        this.userRepository = userRepository;
        this.login = login;
    }

    @PostConstruct
    private String getPresentationResult() throws Exception {
        AtomicReference<String> returnMessage = new AtomicReference<>("");
        //Resource resource = new ClassPathResource("/ports/portConfig.properties");
        //Properties props = PropertiesLoaderUtils.loadProperties(resource);
        //String url = props.getProperty("nats", "");
        String url = "nats://192.168.1.149:4222";

        String[] array = new String[]{url};
        Options opts = new Options.Builder().servers(array).maxReconnects(-1).build();

        Connection connect = Nats.connect(opts);
        Dispatcher dispatcher = connect.createDispatcher(message -> {
            System.out.println(String.format("Received message[%s] from [%s]: ",
                    new String(message.getData(), StandardCharsets.UTF_8), message.getSubject()));

            String proofValueFromVerifier = new String(message.getData(), StandardCharsets.UTF_8);
//            String proofValueFromVerifier = "{\"myDID\":\"connection.myDID\",\"data\":{\"verification_result\":\"ProofValidated\",\"requested_presentation\":{\"revealed_attrs\":{},\"self_attested_attrs\":{\"ID Number\":\"10204000010\"},\"unrevealed_attrs\":{},\"predicates\":{},\"identifiers\":[]},\"@type\":\"did:sov:BzCbsNYhMrjHiqZDTUASHg;spec/present-proof/1.0/presentation-result\",\"@id\":\"6dd15672-cb13-425a-b31c-418ab624be30\",\"~thread\":{\"thid\":\"b8831287-ffa7-4d06-947e-649c7c64e304\",\"sender_order\":0,\"received_orders\":{\"2vwCAwgDUJMQDMPzktJXx1\":0}}},\"threadId\":\"b8831287-ffa7-4d06-947e-649c7c64e304\"}";

            JSONObject obj = new JSONObject(proofValueFromVerifier);
            String myDID = obj.getString("myDID");
            String threadId = obj.getString("threadId");
            System.out.println(threadId);
            String cid = obj.getJSONObject("data").getJSONObject("requested_presentation").getJSONObject("self_attested_attrs").getString("ID Number");

            Optional<User> user = userRepository.findByCid(cid);

            if (user.isPresent()) {
                LoginRequest loginRequest = new LoginRequest();
                loginRequest.setUsername(user.get().getUsername());
                loginRequest.setPassword("12345678");
                try {
                    login.doLogin(loginRequest);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }


        });
        dispatcher.subscribe("gyalsung1160");
        return returnMessage.toString();
    }
}
