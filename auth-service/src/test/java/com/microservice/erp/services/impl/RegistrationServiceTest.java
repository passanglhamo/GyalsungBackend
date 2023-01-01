package com.microservice.erp.services.impl;

import com.microservice.erp.domain.entities.User;
import com.microservice.erp.domain.models.NewAccountRequest;
import com.microservice.erp.domain.models.NewTenantRequest;
import com.microservice.erp.domain.repositories.UserRepository;
import com.infoworks.lab.rest.models.Response;
import com.microservice.erp.services.impl.RegistrationService;
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
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = {TestJPAH2Config.class})
public class RegistrationServiceTest {

    @Mock
    UserRepository repository;
    @Mock
    PasswordEncoder passwordEncoder;

    @InjectMocks
    RegistrationService service;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        ReflectionTestUtils.setField(service, "noReply", "noreply@xyz.com");
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void createNew() {
        when(passwordEncoder.encode(any(CharSequence.class))).thenReturn("$2a$10$ninWnY3IdrhIY3VUgVC2o.lPHKUujgTHCQYgSTHIlbNXXdCbEQBQq");
        //
        NewAccountRequest accountRequest = new NewAccountRequest();
        accountRequest.setUsername("m.towhid@gmail.com");
        accountRequest.setPassword(passwordEncoder.encode("112233"));
        accountRequest.setEmail("m.towhid@gmail.com");
        accountRequest.setMobile("01712645571");
        //
        User user = new User();
        user.unmarshallingFromMap(accountRequest.marshallingToMap(true), true);
        when(repository.findByUsername(any(String.class))).thenReturn(Optional.of(user));
        when(repository.save(any(User.class))).thenReturn(user);
        //
        Response response = service.createNew(accountRequest);
        Assert.assertNotNull(response);
        Assert.assertTrue(response.toString(), response.getStatus() == 200);
        System.out.println(response.getMessage());
    }

    @Test
    public void isAccountExist() {
    }

    @Test
    public void createNewTenant(){
        when(passwordEncoder.encode(any(CharSequence.class))).thenReturn("$2a$10$ninWnY3IdrhIY3VUgVC2o.lPHKUujgTHCQYgSTHIlbNXXdCbEQBQq");
        //
        NewTenantRequest request = new NewTenantRequest();
        request.setEmail("my.tenant@gmail.com");
        request.setMobile("+8801712000071");
        request.setPassword(passwordEncoder.encode("112233"));
        //
        User user = new User();
        user.unmarshallingFromMap(request.marshallingToMap(true), true);
        when(repository.findByUsername(any(String.class))).thenReturn(Optional.of(user));
        when(repository.save(any(User.class))).thenReturn(user);
        //
        Response response = service.createNewTenant(request);
        Assert.assertNotNull(response);
        Assert.assertTrue(response.toString(), response.getStatus() == 200);
    }

    @Test
    public void createNewTenantFailed(){
        when(passwordEncoder.encode(any(CharSequence.class))).thenReturn("$2a$10$ninWnY3IdrhIY3VUgVC2o.lPHKUujgTHCQYgSTHIlbNXXdCbEQBQq");
        //
        NewTenantRequest request = new NewTenantRequest();
        request.setMobile("+8801712000071");
        request.setPassword(passwordEncoder.encode("112233"));
        Response response = service.createNewTenant(request);
        //
        Assert.assertNotNull(response);
        Assert.assertTrue(response.toString(), response.getStatus() == 500);
    }
}