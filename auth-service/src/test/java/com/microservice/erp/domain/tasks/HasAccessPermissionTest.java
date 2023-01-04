package com.microservice.erp.domain.tasks;

import com.microservice.erp.domain.entities.Policy;
import com.microservice.erp.domain.entities.Role;
import com.microservice.erp.domain.entities.Statement;
import com.microservice.erp.domain.entities.User;
import com.microservice.erp.domain.models.AccessPermission;
import com.microservice.erp.domain.models.Action;
import com.microservice.erp.domain.repositories.UserRepository;
import com.microservice.erp.domain.tasks.am.HasAccessPermission;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class HasAccessPermissionTest {

    @Mock
    private UserRepository repository;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void hasPermission() {
        //
        User user = new User();
        user.setUsername("hcafe@gmail.com");
        //user.setFirstName("hCafe");

        Role role = new Role();
        //role.setName("SHOP-ADMIN");
        user.addRoles(role);

        Statement statement1 = new Statement();
        statement1.setAction(Action.Write);
        statement1.setResource("product/add");

        Statement statement2 = new Statement();
        statement2.setAction(Action.Write);
        statement2.setResource("product/update");

        Policy policy = new Policy();
        policy.addStatements(statement1, statement2);
        role.addPolicies(policy);
        //
        when(repository.findByUsername(any(String.class))).thenReturn(Optional.of(user));
        AccessPermission permission = new AccessPermission();
        Statement ss = new Statement();
        ss.setResource("product/add");
        permission.setStatement(ss);
        permission.setUsername(user.getUsername());
        //
        HasAccessPermission hasPermission = new HasAccessPermission(repository, permission);
        AccessPermission nP = hasPermission.execute(null);
        //
        Assert.assertTrue(nP.getMessage(), nP.getStatus() == 200);
        Assert.assertTrue(nP.getStatement().getAction() == Action.Write);
    }

    @Test
    public void hasPermission2() {
        //
        User user = new User();
        user.setUsername("hcafe@gmail.com");
        //user.setFirstName("hCafe");

        Role role1 = new Role();
        //role1.setName("SHOP-ADMIN");
        user.addRoles(role1);

        Statement statement1 = new Statement();
        statement1.setAction(Action.Write);
        statement1.setResource("product/add");

        Statement statement2 = new Statement();
        statement2.setAction(Action.Write);
        statement2.setResource("product/update");

        Policy policy = new Policy();
        policy.addStatements(statement1, statement2);
        role1.addPolicies(policy);

        //.....
        Role role2 = new Role();
        //role2.setName("USER");
        user.addRoles(role2);

        Statement statement3 = new Statement();
        statement3.setAction(Action.Read);
        statement3.setResource("product/add");

        Policy policy2 = new Policy();
        policy2.addStatements(statement3);
        role2.addPolicies(policy2);
        //
        when(repository.findByUsername(any(String.class))).thenReturn(Optional.of(user));
        AccessPermission permission = new AccessPermission();
        Statement ss = new Statement();
        ss.setResource("product/add");
        permission.setStatement(ss);
        permission.setUsername(user.getUsername());
        //
        HasAccessPermission hasPermission = new HasAccessPermission(repository, permission);
        AccessPermission nP = hasPermission.execute(null);
        //
        Assert.assertTrue(nP.getMessage(), nP.getStatus() == 200);
        Assert.assertTrue(nP.getStatement().getAction() == Action.Write);
    }

    @Test
    public void hasPermission3() {
        //
        User user = new User();
        user.setUsername("hcafe@gmail.com");
        //user.setFirstName("hCafe");

        Role role1 = new Role();
        //role1.setName("USER");
        user.addRoles(role1);

        Statement statement1 = new Statement();
        statement1.setAction(Action.Write);
        statement1.setResource("product/add");

        Statement statement2 = new Statement();
        statement2.setAction(Action.None);
        statement2.setResource("product/update");

        Policy policy = new Policy();
        policy.addStatements(statement1, statement2);
        role1.addPolicies(policy);

        //.....
        Role role2 = new Role();
        //role2.setName("SHOP-ADMIN");
        user.addRoles(role2);

        Statement statement3 = new Statement();
        statement3.setAction(Action.Write);
        statement3.setResource("product/add");

        Policy policy2 = new Policy();
        policy2.addStatements(statement3);
        role2.addPolicies(policy2);
        //
        when(repository.findByUsername(any(String.class))).thenReturn(Optional.of(user));
        AccessPermission permission = new AccessPermission();
        Statement ss = new Statement();
        ss.setResource("product/add");
        permission.setStatement(ss);
        permission.setUsername(user.getUsername());
        //
        HasAccessPermission hasPermission = new HasAccessPermission(repository, permission);
        AccessPermission nP = hasPermission.execute(null);
        //
        Assert.assertTrue(nP.getMessage(), nP.getStatus() == 200);
        Assert.assertTrue(nP.getStatement().getAction() == Action.Write);
    }

}
