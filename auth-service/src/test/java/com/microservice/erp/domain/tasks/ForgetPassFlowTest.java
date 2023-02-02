package com.microservice.erp.domain.tasks;

import com.fasterxml.jackson.core.type.TypeReference;
import com.infoworks.lab.beans.tasks.definition.TaskStack;
import com.microservice.erp.domain.entities.User;
import com.microservice.erp.domain.models.LoginRequest;
import com.microservice.erp.domain.repositories.UserRepository;
import com.infoworks.lab.rest.models.Message;
import com.infoworks.lab.rest.models.Response;
import com.microservice.erp.domain.tasks.iam.*;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.matches;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ForgetPassFlowTest extends BaseTaskTest {

    private static Logger LOG = LoggerFactory.getLogger(ForgetPassFlowTest.class);

    @Mock
    private UserRepository repository;

    @Mock
    private PasswordEncoder encoder;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void didForget() {
        when(encoder.encode(any(CharSequence.class))).thenReturn("$2a$10$ninWnY3IdrhIY3VUgVC2o.lPHKUujgTHCQYgSTHIlbNXXdCbEQBQq");
        //
        User user = new User();
        user.setUsername("m@gmail.com");
        user.setEmail("m@gmail.com");
        //user.setMobile("01712645571");
        user.setPassword(encoder.encode("112233"));
        user.setSecrets(createRandomSecrets());
        when(repository.findByUsername(any(String.class))).thenReturn(Optional.of(user));
        //
        Response response = didForget("m@gmail.com");
        Assert.assertTrue(response.toString(), response.getStatus() == 200);
        System.out.println(response.getMessage());
    }

    private Response didForget(String username) {
        Response response = new Response().setMessage("Not Implemented").setStatus(404);
        //
        TaskStack forgetPassStack = TaskStack.createSync(true);
        forgetPassStack.push(new CheckUserExist(repository, username));
        forgetPassStack.push(new ForgotPassword(repository, username));
        /*forgetPassStack.push(new SendEmail(noReply
                , username
                , "Hi There! .... Greetings"
                , "forgot-pass-email-temp-01"));*/
        forgetPassStack.commit(true, (message, state) -> {
            LOG.info("ForgetPassword Status: " + state.name());
            if (message != null){
                response.unmarshallingFromMap(message.marshallingToMap(true), true);
            }
        });
        //
        return response;
    }

    @Test
    public void doReset() throws IOException {
        //
        when(encoder.encode(any(CharSequence.class))).thenReturn("$2a$10$ninWnY3IdrhIY3VUgVC2o.lPHKUujgTHCQYgSTHIlbNXXdCbEQBQq");
        when(encoder.matches(any(CharSequence.class), any(String.class))).thenReturn(true);
        //
        User user = new User();
        user.setUsername("m@gmail.com");
        user.setEmail("m@gmail.com");
        //user.setMobile("01712645571");
        user.setPassword(encoder.encode("112233"));
        user.setSecrets(createRandomSecrets());
        when(repository.findByUsername(any(String.class))).thenReturn(Optional.of(user));
        when(repository.save(any(User.class))).thenReturn(user);
        //
        Response forget = didForget("m@gmail.com");
        Map<String, String> map = Message.unmarshal(new TypeReference<Map<String, String>>() {}, forget.getMessage());
        String forToken = map.get("Reset-Pass-Token");
        //
        Response response = doReset(forToken, "112233", "new-pass");
        Assert.assertTrue(response.toString(), response.getStatus() == 200);
        System.out.println(response.getMessage());
    }

    private Response doReset(String token, String oldPass, String newPass) {
        Response response = new Response().setMessage("Not Implemented").setStatus(404);
        //
        TaskStack resetPassStack = TaskStack.createSync(true);
        resetPassStack.push(new ChangePassword(token, oldPass, newPass, repository, encoder));
        /*resetPassStack.push(new SendEmail(noReply
                , userEmail
                , "Hi There! .... Greetings"
                , "reset-pass-email-temp-01"));*/
        resetPassStack.commit(true, (message, state) -> {
            LOG.info("ChangePassword Status: " + state.name());
            if (message != null){
                response.unmarshallingFromMap(message.marshallingToMap(true), true);
            }
        });
        //
        return response;
    }

    @Test
    public void changePasswordTest() throws IOException {
        //First we create a token for login, keep it for while:
        LoginRequest request = new LoginRequest();
        request.setUsername("m@gmail.com");
        request.setPassword("112233");
        //
        BCryptPasswordEncoder bcEncoder = new BCryptPasswordEncoder();
        String encodedStr = bcEncoder.encode("112233");
        when(encoder.encode(matches("112233"))).thenReturn(encodedStr);
        when(encoder.matches("112233", encodedStr)).thenReturn(true);
        //
        User user = new User();
        user.setUsername("m@gmail.com");
        user.setPassword(encoder.encode("112233"));
        user.setEmail("m@gmail.com");
        //user.setMobile("01712645571");
        user.setEnabled(true);
        user.setSecrets(createRandomSecrets());
        when(repository.findByUsername(any(String.class))).thenReturn(Optional.of(user));
        //
        Response doLogin = doLogin(request);
        Assert.assertTrue(doLogin.toString(), doLogin.getStatus() == 200);
        Map<String, String> data = Message.unmarshal(new TypeReference<Map<String, String>>() {}, doLogin.getMessage());
        String oldToken = data.get("X-Auth-Token");
        //
        //Then we change/update user password in persistence:
        String encodedStr2 = bcEncoder.encode("new-pass");
        when(encoder.encode(matches("new-pass"))).thenReturn(encodedStr2);
        //when(encoder.matches("new-pass", encodedStr2)).thenReturn(true);
        //
        User user1 = new User();
        user1.setUsername("m@gmail.com");
        user1.setEmail("m@gmail.com");
        //user1.setMobile("01712645571");
        user1.setPassword(encoder.encode("112233"));
        user1.setSecrets(createRandomSecrets());
        when(repository.findByUsername(any(String.class))).thenReturn(Optional.of(user1));
        when(repository.save(any(User.class))).thenReturn(user1);
        //
        Response forget = didForget("m@gmail.com");
        Map<String, String> map = Message.unmarshal(new TypeReference<Map<String, String>>() {}, forget.getMessage());
        String forToken = map.get("Reset-Pass-Token");
        //
        Response doReset = doReset(forToken, "112233", "new-pass");
        Assert.assertTrue(doReset.toString(), doReset.getStatus() == 200);
        Map<String, String> data2 = Message.unmarshal(new TypeReference<Map<String, String>>() {}, doReset.getMessage());
        String newToken = data2.get("X-Auth-Token");
        //Check both token:
        Assert.assertTrue(!oldToken.equalsIgnoreCase(newToken));
        //
        //Old Token will not be validated:
        CheckTokenValidity checkTokenValidity = new CheckTokenValidity(oldToken, repository);
        Response validityResponse = checkTokenValidity.execute(null);
        Assert.assertTrue(validityResponse.getMessage(), validityResponse.getStatus() == 401);
        //
        //New Token will be validated:
        checkTokenValidity = new CheckTokenValidity(newToken, repository);
        validityResponse = checkTokenValidity.execute(null);
        Assert.assertTrue(validityResponse.getMessage(), validityResponse.getStatus() == 200);
    }

    private Response doLogin(LoginRequest request) {
        Response response = new Response().setMessage("Not Implemented").setStatus(404);
        //
        TaskStack loginStack = TaskStack.createSync(true);
        loginStack.push(new CheckUserExist(repository, request.getUsername()));
        loginStack.push(new Login(repository, encoder, null,false,request));
        loginStack.commit(true, (message, state) -> {
            LOG.info("Login Status: " + state.name());
            if (message != null)
                response.unmarshallingFromMap(message.marshallingToMap(true), true);
        });
        //
        return response;
    }

}
