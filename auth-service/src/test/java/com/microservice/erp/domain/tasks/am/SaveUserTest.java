//package com.microservice.erp.domain.tasks.am;
//
//import com.infoworks.lab.beans.tasks.definition.Task;
//import com.microservice.erp.domain.entities.User;
//import com.microservice.erp.domain.repositories.UserRepository;
//import com.infoworks.lab.rest.models.Message;
//import com.infoworks.lab.rest.models.Response;
//import org.junit.Assert;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.mockito.junit.MockitoJUnitRunner;
//import org.springframework.security.crypto.password.PasswordEncoder;
//
//import java.util.Optional;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.when;
//
//@RunWith(MockitoJUnitRunner.class)
//public class SaveUserTest {
//
//    @Before
//    public void setUp() throws Exception {
//        MockitoAnnotations.initMocks(this);
//    }
//
//    @Mock private UserRepository repository;
//
//    @Mock private PasswordEncoder encoder;
//
//    @Test
//    public void execute() {
//        //
//        when(encoder.encode(any(CharSequence.class))).thenReturn("$2a$10$rM6UzzKoB4iVmEJ9sjLnYeBo4GdLfZH1lh1UjF9Gepa8FHW9YDObi");
//        User user = new User();
//        user.setUsername("m@gmail.com");
//        user.setPassword(encoder.encode("112233"));
//        user.setEmail("m@gmail.com");
//        user.setMobile("01712645571");
//        user.setEnabled(true);
//        when(repository.findByUsername(any(String.class))).thenReturn(Optional.empty());
//        //
//        User user2 = new User();
//        user2.unmarshallingFromMap(user.marshallingToMap(true), true);
//        user2.setId(101l);
//        when(repository.save(any(User.class))).thenReturn(user2);
//        //
//        Task<Message, Response> task = new SaveUser(repository, user);
//        Response response = task.execute(null);
//        //
//        Assert.assertTrue(response.toString(), response.getStatus() == 200);
//        System.out.println(response.toString());
//    }
//
//    @Test
//    public void abort() {
//        when(encoder.encode(any(CharSequence.class))).thenReturn("$2a$10$rM6UzzKoB4iVmEJ9sjLnYeBo4GdLfZH1lh1UjF9Gepa8FHW9YDObi");
//        User user = new User();
//        user.setId(101l);
//        user.setUsername("m@gmail.com");
//        user.setPassword(encoder.encode("112233"));
//        user.setEmail("m@gmail.com");
//        user.setMobile("01712645571");
//        user.setEnabled(true);
//        //when(repository.findByUsername(any(String.class))).thenReturn(Optional.of(user));
//        //
//        Task<Message, Response> task = new SaveUser(repository, user);
//        Response response = task.abort(null);
//        //
//        Assert.assertTrue(response.toString(), response.getStatus() == 500);
//        System.out.println(response.toString());
//    }
//}