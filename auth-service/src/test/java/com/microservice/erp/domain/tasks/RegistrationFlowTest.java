//package com.microservice.erp.domain.tasks;
//
//import com.infoworks.lab.beans.tasks.definition.TaskStack;
//import com.microservice.erp.domain.entities.User;
//import com.microservice.erp.domain.models.NewAccountRequest;
//import com.microservice.erp.domain.models.NewTenantRequest;
//import com.microservice.erp.domain.repositories.UserRepository;
//import com.infoworks.lab.rest.models.Response;
//import com.microservice.erp.domain.tasks.iam.CheckUserExist;
//import com.microservice.erp.domain.tasks.iam.CreateNewTenant;
//import com.microservice.erp.domain.tasks.iam.CreateNewUser;
//import org.junit.After;
//import org.junit.Assert;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.mockito.junit.MockitoJUnitRunner;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.security.crypto.password.PasswordEncoder;
//
//import java.util.Optional;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.when;
//
//@RunWith(MockitoJUnitRunner.class)
//public class RegistrationFlowTest {
//
//    private static Logger LOG = LoggerFactory.getLogger(RegistrationFlowTest.class);
//
//    @Mock
//    UserRepository repository;
//
//    @Mock
//    PasswordEncoder passwordEncoder;
//
//    @Before
//    public void setUp() throws Exception {
//        MockitoAnnotations.initMocks(this);
//    }
//
//    @After
//    public void tearDown() throws Exception {
//    }
//
//    @Test
//    public void createNew() {
//        NewAccountRequest accountRequest = new NewAccountRequest();
//        accountRequest.setUsername("m.towhid@gmail.com");
//        accountRequest.setPassword("112233");
//        accountRequest.setEmail("m.towhid@gmail.com");
//        accountRequest.setMobile("01712645571");
//        //
//        User user = new User();
//        user.unmarshallingFromMap(accountRequest.marshallingToMap(true), true);
//        when(repository.findByUsername(any(String.class))).thenReturn(Optional.ofNullable(null));
//        when(repository.save(any(User.class))).thenReturn(user);
//        when(passwordEncoder.encode(any(CharSequence.class))).thenReturn("$2a$10$ninWnY3IdrhIY3VUgVC2o.lPHKUujgTHCQYgSTHIlbNXXdCbEQBQq");
//        //
//        Response response = createNew(accountRequest);
//        Assert.assertNotNull(response);
//        Assert.assertTrue(response.toString(), response.getStatus() == 200);
//        System.out.println(response.getMessage());
//    }
//
//    private Response createNew(NewAccountRequest account) {
//        final Response response = new Response()
//                .setStatus(404)
//                .setMessage("Under Construction!");
//        //
//        TaskStack stack = TaskStack.createSync(true);
//        stack.push(new CheckUserExist(repository, account));
//        stack.push(new CreateNewUser(passwordEncoder, repository, account));
//        //stack.push(new SendEmail(noReply, account.getEmail(), "greetings text message!", "template-id"));
//        stack.commit(true, (message, state) -> {
//            LOG.info("Account Creating Status: " + state.name());
//            if (message != null){
//                response.unmarshallingFromMap(message.marshallingToMap(true), true);
//            }
//        });
//        return response;
//    }
//
//    @Test
//    public void isAccountExist() {
//    }
//
//    private Response isAccountExist(String username) {
//        return new CheckUserExist(repository, username).execute(null);
//    }
//
//    @Test
//    public void createNewTenant(){
//        NewTenantRequest request = new NewTenantRequest();
//        request.setEmail("my.tenant@gmail.com");
//        request.setMobile("+8801712000071");
//        request.setPassword("****");
//        //
//        User user = new User();
//        user.unmarshallingFromMap(request.marshallingToMap(true), true);
//        when(repository.findByUsername(any(String.class))).thenReturn(Optional.of(user));
//        //when(repository.save(any(User.class))).thenReturn(user);
//        when(passwordEncoder.encode(any(CharSequence.class))).thenReturn("$2a$10$ninWnY3IdrhIY3VUgVC2o.lPHKUujgTHCQYgSTHIlbNXXdCbEQBQq");
//        //
//        Response response = createNewTenant(request);
//        Assert.assertNotNull(response);
//        Assert.assertTrue(response.toString(), response.getStatus() == 200);
//    }
//
//    @Test
//    public void createNewTenantFailed(){
//        NewTenantRequest request = new NewTenantRequest();
//        request.setMobile("+8801712000071");
//        request.setPassword("****");
//        Response response = createNewTenant(request);
//        //
//        Assert.assertNotNull(response);
//        Assert.assertTrue(response.toString(), response.getStatus() == 500);
//    }
//
//    private Response createNewTenant(NewTenantRequest tenant) {
//        final Response response = new Response()
//                .setStatus(404)
//                .setMessage("Under Construction!");
//        //
//        TaskStack stack = TaskStack.createSync(true);
//        stack.push(new CheckUserExist(repository, tenant.getEmail()));
//        stack.push(new CreateNewTenant(passwordEncoder, null, tenant));
//        stack.push(new CreateNewUser(passwordEncoder, repository));
//        //TODO:
//        //stack.push(new SendEmail(noReply, tenant.getEmail(), "greetings text message!", "template-id"));
//        stack.commit(true, (message, state) -> {
//            LOG.info("Tenant Creating Status: " + state.name());
//            if (message != null){
//                response.unmarshallingFromMap(message.marshallingToMap(true), true);
//            }
//        });
//        return response;
//    }
//
//}
