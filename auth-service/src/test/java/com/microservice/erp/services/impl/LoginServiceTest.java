package com.microservice.erp.services.impl;

import com.microservice.erp.domain.entities.User;
import com.microservice.erp.domain.models.LoginRequest;
import com.microservice.erp.domain.repositories.UserRepository;
import com.infoworks.lab.rest.models.Response;
import com.microservice.erp.webapp.config.TestJPAH2Config;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = {TestJPAH2Config.class})
public class LoginServiceTest extends com.microservice.erp.services.impl.BaseServiceTest {

    @Mock
    private UserRepository repository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private LoginService service;

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
        //user.setMobile("01712645571");
        user.setEnabled(true);
        user.setSecrets(createRandomSecrets());
        when(repository.findByUsername(any(String.class))).thenReturn(Optional.of(user));
        //
//        Response response = service.doLogin(request);
//        Assert.assertTrue(response.toString(), response.getStatus() == 200);
//        System.out.println(response.getMessage());
    }

    @Test
    public void isValidToken() {
        when(passwordEncoder.encode(any(CharSequence.class))).thenReturn("$2a$10$ninWnY3IdrhIY3VUgVC2o.lPHKUujgTHCQYgSTHIlbNXXdCbEQBQq");
        //
        User user = new User();
        user.setUsername("m@gmail.com");
        //user.setMobile("01712645571");
        user.setPassword(passwordEncoder.encode("112233"));
        user.setSecrets(createRandomSecrets());
        when(repository.findByUsername(any(String.class))).thenReturn(Optional.of(user));
        //
        String token = createToken(user);
        Response response = service.isValidToken(token, user);
        Assert.assertTrue(response.toString(), response.getStatus() == 200);
        System.out.println(response.getMessage());
    }

    @Test
    public void refreshToken() {
        when(passwordEncoder.encode(any(CharSequence.class))).thenReturn("$2a$10$ninWnY3IdrhIY3VUgVC2o.lPHKUujgTHCQYgSTHIlbNXXdCbEQBQq");
        //
        User user = new User();
        user.setUsername("m@gmail.com");
        //user.setMobile("01712645571");
        user.setPassword(passwordEncoder.encode("112233"));
        user.setSecrets(createRandomSecrets());
        when(repository.findByUsername(any(String.class))).thenReturn(Optional.of(user));
        //
        String token = createToken(user);
        Response response = service.refreshToken(token, user);
        Assert.assertTrue(response.toString(), response.getStatus() == 200);
        System.out.println(response.getMessage());
    }

    @Test
    public void doLogout() {
        when(passwordEncoder.encode(any(CharSequence.class))).thenReturn("$2a$10$ninWnY3IdrhIY3VUgVC2o.lPHKUujgTHCQYgSTHIlbNXXdCbEQBQq");
        //
        User user = new User();
        user.setUsername("m@gmail.com");
        //user.setMobile("01712645571");
        user.setPassword(passwordEncoder.encode("112233"));
        user.setSecrets(createRandomSecrets());
        when(repository.findByUsername(any(String.class))).thenReturn(Optional.of(user));
        //
        String token = createToken(user);
        Response response = service.doLogout(token, user);
        Assert.assertTrue(200 == response.getStatus());
    }
}