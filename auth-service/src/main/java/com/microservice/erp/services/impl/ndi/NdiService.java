package com.microservice.erp.services.impl.ndi;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservice.erp.domain.dto.NdiProofResponseDto;
import com.microservice.erp.domain.entities.User;
import com.microservice.erp.domain.models.LoginRequest;
import com.microservice.erp.domain.repositories.UserRepository;
import com.microservice.erp.services.definition.INdiService;
import com.microservice.erp.services.definition.iLogin;
import io.nats.client.Connection;
import io.nats.client.Dispatcher;
import io.nats.client.Nats;
import io.nats.client.Options;
import org.json.JSONObject;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class NdiService implements INdiService {
    private UserRepository userRepository;
    private iLogin login;


    public NdiService(UserRepository userRepository, iLogin login) {
        this.userRepository = userRepository;
        this.login = login;
    }

    public ResponseEntity<?> getProofRequest() throws IOException, InterruptedException {
        RestTemplate restTemplate = new RestTemplate();
        String requestString = "{\n" +
                " \"proofName\": \"TTPL\",\n" +
                " \"proofAttributes\": [\n" +
                " {\n" +
                " \"name\":\"ID Number\"," +
                " \"restrictions\":[]\n" +
                "}]\n" +
                "}";

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);

        Resource resource = new ClassPathResource("/ndiapi/ndiApi.properties");
        Properties props = PropertiesLoaderUtils.loadProperties(resource);
        String verifyNdi = props.getProperty("verifyNDI.endPointURL");

        HttpEntity<String> requestEntity = new HttpEntity<>(requestString, headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange(
                verifyNdi, HttpMethod.POST, requestEntity, String.class
        );


        JSONObject obj = new JSONObject(responseEntity.getBody());
        String threadId = obj.getJSONObject("data").getString("proofRequestThreadId");
        String proofRequestURL = obj.getJSONObject("data").getString("proofRequestURL");

        NdiProofResponseDto proofResponseDto = new NdiProofResponseDto();
        proofResponseDto.setProofUrl(proofRequestURL);
        proofResponseDto.setThreadId(threadId);

        getPresentationResult(threadId);

        return ResponseEntity.ok(proofResponseDto);

    }

    public String getPresentationResult(String threadId) throws IOException, InterruptedException {
        AtomicReference<String> returnMessage = new AtomicReference<>(  "");
        Resource resource = new ClassPathResource("/ndiapi/ndiApi.properties");
        Properties props = PropertiesLoaderUtils.loadProperties(resource);
        String natsUrl = props.getProperty("natsUrl.endPointURL");
        String[] array = new String[]{natsUrl};
        Options opts = new Options.Builder().servers(array).maxReconnects(-1).build();

        Connection connect = Nats.connect(opts);
        Dispatcher dispatcher = connect.createDispatcher(message -> {
            System.out.println(String.format("Received message[%s] from [%s]: ",
                    new String(message.getData(), StandardCharsets.UTF_8), message.getSubject()));
            ObjectMapper mapper = new ObjectMapper();

            String proofValueFromVerifier = new String(message.getData(), StandardCharsets.UTF_8);

            JSONObject obj = new JSONObject(proofValueFromVerifier);

            //todo need to add self attr
//            String cid = obj.getJSONObject("data").getJSONObject("requested_presentation")
//                    .getJSONObject("revealed_attrs").getJSONObject("ID Number")
//                    .getString("value");
            String cid = obj.getJSONObject("data").getJSONObject("requested_presentation").getJSONObject("self_attested_attrs").getString("ID Number");

            Optional<User> user = userRepository.findByCid(cid);

            if (user.isPresent()) {
                LoginRequest loginRequest = new LoginRequest();
                loginRequest.setUsername(user.get().getUsername());
                try {
                    ResponseEntity<?> response = login.doLogin(loginRequest,true);

                    connect.publish("loginData"+threadId,
                            mapper.writeValueAsString(Objects.requireNonNull(response.getBody())).getBytes());
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }


        });
        dispatcher.subscribe(threadId);
        return returnMessage.toString();
    }
}
