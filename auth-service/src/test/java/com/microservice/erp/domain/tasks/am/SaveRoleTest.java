//package com.microservice.erp.domain.tasks.am;
//
//import com.infoworks.lab.beans.tasks.definition.Task;
//import com.microservice.erp.domain.entities.Role;
//import com.microservice.erp.domain.repositories.RoleRepository;
//import com.infoworks.lab.rest.models.Message;
//import com.infoworks.lab.rest.models.Response;
//import org.junit.Assert;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.mockito.junit.MockitoJUnitRunner;
//
//import java.util.Optional;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.when;
//
//@RunWith(MockitoJUnitRunner.class)
//public class SaveRoleTest {
//
//    @Before
//    public void setUp() throws Exception {
//        MockitoAnnotations.initMocks(this);
//    }
//
//    @Mock
//    private RoleRepository repository;
//
//    @Test
//    public void execute() {
//        Role role = new Role();
//        role.setName("SHOP-ADMIN");
//        when(repository.findRoleByName(any(String.class))).thenReturn(Optional.empty());
//
//        Role role2 = new Role();
//        role2.unmarshallingFromMap(role.marshallingToMap(true), true);
//        role2.setId(101l);
//        when(repository.save(any(Role.class))).thenReturn(role2);
//        //
//        Task<Message, Response> task = new SaveRole(repository, role);
//        Response response = task.execute(null);
//        //
//        Assert.assertTrue(response.toString(), response.getStatus() == 200);
//        System.out.println(response.toString());
//    }
//
//    @Test
//    public void abort() {
//    }
//}