package com.microservice.erp.domain.tasks;

import com.infoworks.lab.beans.tasks.definition.TaskStack;
import com.microservice.erp.domain.entities.User;
import com.microservice.erp.domain.models.LoginRequest;
import com.microservice.erp.domain.repositories.UserRepository;
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
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class LoginFlowTest extends BaseTaskTest{

    private static Logger LOG = LoggerFactory.getLogger(LoginFlowTest.class);

    @Mock
    private UserRepository repository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void doLogin() {
        LoginRequest request = new LoginRequest();
        request.setUsername("m@gmail.com");
        request.setPassword("112233");
        //
        when(passwordEncoder.encode(any(CharSequence.class))).thenReturn("$2a$10$ninWnY3IdrhIY3VUgVC2o.lPHKUujgTHCQYgSTHIlbNXXdCbEQBQq");
        when(passwordEncoder.matches(any(CharSequence.class), any(String.class))).thenReturn(true);
        //
        User user = new User();
        user.setUsername("m@gmail.com");
        user.setPassword(passwordEncoder.encode("112233"));
        user.setEmail("m@gmail.com");
        user.setMobile("01712645571");
        user.setEnabled(true);
        user.setSecrets(createRandomSecrets());
        when(repository.findByUsername(any(String.class))).thenReturn(Optional.of(user));
        //
        Response response = doLogin(request);
        Assert.assertTrue(response.toString(), response.getStatus() == 200);
        System.out.println(response.getMessage());
    }

    private Response doLogin(LoginRequest request) {
        Response response = new Response().setMessage("Not Implemented").setStatus(404);
        //
        TaskStack loginStack = TaskStack.createSync(true);
        loginStack.push(new CheckUserExist(repository, request.getUsername()));
        loginStack.push(new Login(repository, passwordEncoder, request));
        loginStack.commit(true, (message, state) -> {
            LOG.info("Login Status: " + state.name());
            if (message != null)
                response.unmarshallingFromMap(message.marshallingToMap(true), true);
        });
        //
        return response;
    }

    @Test
    public void isValidToken() {
        when(passwordEncoder.encode(any(CharSequence.class))).thenReturn("$2a$10$ninWnY3IdrhIY3VUgVC2o.lPHKUujgTHCQYgSTHIlbNXXdCbEQBQq");
        //
        User user = new User();
        user.setUsername("m@gmail.com");
        user.setMobile("01712645571");
        user.setPassword(passwordEncoder.encode("112233"));
        user.setSecrets(createRandomSecrets());
        when(repository.findByUsername(any(String.class))).thenReturn(Optional.of(user));
        //
        String token = createToken(user);
        Response response = isValidToken(token, user);
        Assert.assertTrue(response.toString(), response.getStatus() == 200);
        System.out.println(response.getMessage());
    }

    private Response isValidToken(String token, UserDetails principal) {
        return new CheckTokenValidity(token, repository).execute(null);
    }

    @Test
    public void refreshToken() {
        when(passwordEncoder.encode(any(CharSequence.class))).thenReturn("$2a$10$ninWnY3IdrhIY3VUgVC2o.lPHKUujgTHCQYgSTHIlbNXXdCbEQBQq");
        //
        User user = new User();
        user.setUsername("m@gmail.com");
        user.setMobile("01712645571");
        user.setPassword(passwordEncoder.encode("112233"));
        user.setSecrets(createRandomSecrets());
        when(repository.findByUsername(any(String.class))).thenReturn(Optional.of(user));
        //
        String token = createToken(user);
        Response response = refreshToken(token, user);
        Assert.assertTrue(response.toString(), response.getStatus() == 200);
        System.out.println(response.getMessage());
    }

    private Response refreshToken(String token, UserDetails principal) {
        return new RefreshToken(token, repository).execute(null);
    }

    @Test
    public void doLogout() {
        when(passwordEncoder.encode(any(CharSequence.class))).thenReturn("$2a$10$ninWnY3IdrhIY3VUgVC2o.lPHKUujgTHCQYgSTHIlbNXXdCbEQBQq");
        //
        User user = new User();
        user.setUsername("m@gmail.com");
        user.setMobile("01712645571");
        user.setPassword(passwordEncoder.encode("112233"));
        user.setSecrets(createRandomSecrets());
        when(repository.findByUsername(any(String.class))).thenReturn(Optional.of(user));
        //
        String token = createToken(user);
        Response response = doLogout(token, user);
        Assert.assertTrue(200 == response.getStatus());
    }

    private Response doLogout(String token, UserDetails principal) {
        Response response = new Response().setMessage("Not Implemented").setStatus(404);
        //
        TaskStack logoutStack = TaskStack.createSync(true);
        logoutStack.push(new CheckTokenValidity(token, repository));
        logoutStack.push(new Logout(token));
        logoutStack.push(new MakeTokenExpired(token));
        logoutStack.commit(true, (message, state) -> {
            LOG.info("Logout Status: " + state.name());
            if (message != null)
                response.unmarshallingFromMap(message.marshallingToMap(true), true);
        });
        //
        return response;
    }

    @Test
    public void changePassword(){
        when(passwordEncoder.encode(any(CharSequence.class))).thenReturn("$2a$10$ninWnY3IdrhIY3VUgVC2o.lPHKUujgTHCQYgSTHIlbNXXdCbEQBQq");
        when(passwordEncoder.matches(any(CharSequence.class), any(String.class))).thenReturn(true);
        //
        User user = new User();
        user.setUsername("m@gmail.com");
        user.setEmail("m@gmail.com");
        user.setMobile("01712645571");
        user.setPassword(passwordEncoder.encode("123456"));
        user.setSecrets(createRandomSecrets());
        when(repository.findByUsername(any(String.class))).thenReturn(Optional.of(user));
        when(repository.save(any(User.class))).thenReturn(user);
        //
        String token = createToken(user);
        Response response = changePassword(token, "123456", "654321");
        Assert.assertTrue(200 == response.getStatus());
    }

    private Response changePassword(String token, String oldPass, String newPass) {
        Response response = new Response().setMessage("Not Implemented").setStatus(404);
        //
        TaskStack changePassStack = TaskStack.createSync(true);
        changePassStack.push(new CheckTokenValidity(token, repository));
        changePassStack.push(new ChangePassword(token, oldPass, newPass, repository, passwordEncoder));
        changePassStack.commit(true, (message, state) -> {
            LOG.info("ChangePassword Status: " + state.name());
            if (message != null)
                response.unmarshallingFromMap(message.marshallingToMap(true), true);
        });
        //
        return response;
    }

}
