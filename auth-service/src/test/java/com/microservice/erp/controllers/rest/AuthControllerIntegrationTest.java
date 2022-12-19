package com.microservice.erp.controllers.rest;

import com.fasterxml.jackson.core.type.TypeReference;
import com.microservice.erp.controllers.rest.AuthController;
import com.microservice.erp.domain.models.ChangePassRequest;
import com.microservice.erp.domain.models.LoginRetryCount;
import com.microservice.erp.domain.entities.Policy;
import com.microservice.erp.domain.entities.Role;
import com.microservice.erp.domain.entities.Statement;
import com.microservice.erp.domain.entities.User;
import com.microservice.erp.domain.models.Action;
import com.microservice.erp.domain.models.LoginRequest;
import com.microservice.erp.domain.repositories.RoleRepository;
import com.microservice.erp.domain.repositories.UserRepository;
import com.infoworks.lab.rest.models.Message;
import com.infoworks.lab.rest.models.Response;
import com.microservice.erp.services.impl.BaseServiceTest;
//import com.microservice.erp.webapp.JwtAuthApiTest;
//import com.microservice.erp.webapp.config.BeanConfig;
//import com.microservice.erp.webapp.config.JWTokenValidator;
//import com.microservice.erp.webapp.config.SecurityConfig;
//import com.microservice.erp.webapp.config.TestJPAConfig;
import com.it.soul.lab.data.base.DataSource;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

@RunWith(SpringJUnit4ClassRunner.class)
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE
//        , classes = {JwtAuthApiTest.class, BeanConfig.class
//        , SecurityConfig.class, JWTokenValidator.class
//        , TestJPAConfig.class})
@TestPropertySource(locations = {"classpath:application.properties"})
public class AuthControllerIntegrationTest extends BaseServiceTest {

    @Before
    public void setUp() throws Exception {
        System.out.println(env.getProperty("spring.application.name"));
        //Insert Some Items:
        User user = new User();
        user.setUsername("01712645571");
        user.setPassword(passwordEncoder.encode("112233"));
        user.setEmail("m@gmail.com");
        user.setMobile("01712645571");
        user.setEnabled(true);
        user.setSecrets(createRandomSecrets());
        //
        Role role = new Role();
        role.setName("ADMIN");
        user.addRoles(role);
        //
        Statement statement = new Statement();
        statement.setAction(Action.Write);
        statement.setResource("product/add");
        //
        Policy policy = new Policy();
        policy.setServiceName("product");
        policy.addStatements(statement);
        role.addPolicies(policy);
        //
        userRepository.save(user);
    }

    @After
    public void cleanup() throws Exception {
        Optional<User> opt = userRepository.findByUsername("01712645571");
        if(opt.isPresent()) {
            userRepository.delete(opt.get());
        }
    }

    @Autowired
    private DataSource<String, LoginRetryCount> cache;

    @Autowired
    private AuthController controller;

    @Autowired
    private Environment env;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    public void test(){
        System.out.println("User count: " + userRepository.count());
        Optional<User> user = userRepository.findByUsername("01712645571");
        if (user.isPresent()) {
            Optional<Role> role = roleRepository.findRoleByName("ADMIN");
            if (role.isPresent()){
                Optional<Policy> policy = role.get().getPolicies().stream().findFirst();
                System.out.println(policy.get().toString());
            }
        }
    }

    @Test
    public void isExistTest() throws IOException {
        ResponseEntity<String> rep = controller.isExist("01712645571");
        System.out.println(rep.toString());
        Response response = Message.unmarshal(Response.class, rep.getBody());
        Assert.assertTrue("IsExist Not Success", response.getStatus() == 200);
    }

    @Test
    public void loginTest() throws IOException {
        LoginRequest request = new LoginRequest();
        request.setUsername("01712645571");
        request.setPassword("112233");
        ResponseEntity<?> rep = controller.login(request);
        System.out.println(rep.toString());
        Response response = Message.unmarshal(Response.class, (String) rep.getBody());
        Assert.assertTrue("loginTest Not Success", response.getStatus() == 200);
    }

    @Test
    public void loginFailedTest() throws IOException {
        LoginRequest request = new LoginRequest();
        request.setUsername("Test_TMR2");
        request.setPassword("4321091");
        ResponseEntity<?> rep = controller.login(request);
        System.out.println(rep.toString());
        Response response = Message.unmarshal(Response.class, (String) rep.getBody());
        Assert.assertTrue("loginFailedTest Not Success", response.getStatus() == 500);
    }

    @Test
    public void loginUsernameFailedTest() throws IOException {
        LoginRequest request = new LoginRequest();
        request.setUsername("Test_TMR3");
        request.setPassword("4321");
        ResponseEntity<?> rep = controller.login(request);
        System.out.println(rep.toString());
        Response response = Message.unmarshal(Response.class, (String) rep.getBody());
        Assert.assertTrue("loginUsernameFailedTest Not Success", response.getStatus() == 500);
    }

    @Test
    public void forgetAndResetTest() throws IOException {
//        ResponseEntity<String> rep = controller.forget("01712645571");
//        System.out.println(rep.toString());
//        Response response = Message.unmarshal(Response.class, rep.getBody());
//        Assert.assertTrue("forgetTest Not Success", response.getStatus() == 200);
//        //
//        Map<String, String> data = Message.unmarshal(new TypeReference<Map<String, String>>() {}, response.getMessage());
//        String resetToken = data.get("Reset-Pass-Token");
//        //
//        ResponseEntity<String> reset = controller.reset(resetToken, new ChangePassRequest("112233", "1234"));
//        System.out.println(reset.toString());
//        Response response1 = Message.unmarshal(Response.class, reset.getBody());
//        Assert.assertTrue("reset Not Success", response1.getStatus() == 200);
//        //
//        //Revert the process:
//        rep = controller.forget("01712645571");
//        response = Message.unmarshal(Response.class, rep.getBody());
//        data = Message.unmarshal(new TypeReference<Map<String, String>>() {}, response.getMessage());
//        resetToken = data.get("Reset-Pass-Token");
//        reset = controller.reset(resetToken, new ChangePassRequest("1234", "112233"));
//        System.out.println(reset.toString());
    }
}