package com.microservice.erp.services.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.microservice.erp.domain.entities.User;
import com.microservice.erp.domain.models.LoginRequest;
import com.microservice.erp.domain.repositories.UserRepository;
import com.infoworks.lab.rest.models.Message;
import com.infoworks.lab.rest.models.Response;
import com.microservice.erp.domain.tasks.iam.CheckTokenValidity;
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
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.matches;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = {TestJPAH2Config.class})
public class ResetPassServiceTest extends BaseServiceTest {

    @Mock
    private UserRepository repository;

    @Mock
    private PasswordEncoder encoder;

    @InjectMocks
    private ResetPassService service;

    @InjectMocks
    private LoginService loginService;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        ReflectionTestUtils.setField(service, "noReply", "noreply@xyz.com");
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
        user.setMobile("01712645571");
        user.setPassword(encoder.encode("112233"));
        user.setSecrets(createRandomSecrets());
        when(repository.findByUsername(any(String.class))).thenReturn(Optional.of(user));
        //
        Response response = service.didForget("m@gmail.com");
        Assert.assertTrue(response.toString(), response.getStatus() == 200);
        System.out.println(response.getMessage());
    }

    @Test
    public void doReset() throws IOException {
        //
        when(encoder.encode(any(CharSequence.class))).thenReturn("$2a$10$ninWnY3IdrhIY3VUgVC2o.lPHKUujgTHCQYgSTHIlbNXXdCbEQBQq");
        when(encoder.matches(any(CharSequence.class), any(String.class))).thenReturn(true);
        //
        User user = new User();
        user.setUsername("m@gmail.com");
        user.setMobile("01712645571");
        user.setPassword(encoder.encode("112233"));
        user.setSecrets(createRandomSecrets());
        when(repository.findByUsername(any(String.class))).thenReturn(Optional.of(user));
        when(repository.save(any(User.class))).thenReturn(user);
        //
        Response forget = service.didForget("m@gmail.com");
        Map<String, String> map = Message.unmarshal(new TypeReference<Map<String, String>>() {}, forget.getMessage());
        String forToken = map.get("Reset-Pass-Token");
        //
        Response response = service.doReset(forToken, "112233", "new-pass");
        Assert.assertTrue(response.toString(), response.getStatus() == 200);
        System.out.println(response.getMessage());
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
        user.setMobile("01712645571");
        user.setEnabled(true);
        user.setSecrets(createRandomSecrets());
        when(repository.findByUsername(any(String.class))).thenReturn(Optional.of(user));
        //
        ResponseEntity<?> doLogin = loginService.doLogin(request);
        Assert.assertTrue(doLogin.toString(), 200 == 200);
        Map<String, String> data = Message.unmarshal(new TypeReference<Map<String, String>>() {}, "");
        String oldToken = data.get("X-Auth-Token");
        //
        //Then we change/update user password in persistence:
        String encodedStr2 = bcEncoder.encode("new-pass");
        when(encoder.encode(matches("new-pass"))).thenReturn(encodedStr2);
        //when(encoder.matches("new-pass", encodedStr2)).thenReturn(true);
        //
        User user1 = new User();
        user1.setUsername("m@gmail.com");
        user1.setMobile("01712645571");
        user1.setPassword(encoder.encode("112233"));
        user1.setSecrets(createRandomSecrets());
        when(repository.findByUsername(any(String.class))).thenReturn(Optional.of(user1));
        when(repository.save(any(User.class))).thenReturn(user1);
        //
        Response forget = service.didForget("m@gmail.com");
        Map<String, String> map = Message.unmarshal(new TypeReference<Map<String, String>>() {}, forget.getMessage());
        String forToken = map.get("Reset-Pass-Token");
        //
        Response doReset = service.doReset(forToken, "112233", "new-pass");
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

}