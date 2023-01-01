package com.microservice.erp.domain.tasks.am;

import com.infoworks.lab.beans.tasks.definition.TaskStack;
import com.microservice.erp.domain.entities.Policy;
import com.microservice.erp.domain.entities.Role;
import com.microservice.erp.domain.entities.Statement;
import com.microservice.erp.domain.entities.User;
import com.microservice.erp.domain.models.Action;
import com.microservice.erp.domain.repositories.PolicyRepository;
import com.microservice.erp.domain.repositories.RoleRepository;
import com.microservice.erp.domain.repositories.UserRepository;
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
public class UserFullTest {

    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);
    }

    @Mock private UserRepository userRepository;
    @Mock private UserRepository userRepository1;
    @Mock private RoleRepository roleRepository;
    @Mock private RoleRepository roleRepository1;
    @Mock private PolicyRepository policyRepository;
    @Mock private PolicyRepository policyRepository1;

    @Test
    public void fullTest(){

        User user = new User();
        user.setUsername("hcafe@gmail.com");
        user.setFirstName("hCafe");
        when(userRepository.findByUsername(any(String.class))).thenReturn(Optional.empty());

        User user1 = new User();
        user1.unmarshallingFromMap(user.marshallingToMap(true), true);
        user1.setId(101l);
        when(userRepository.save(any(User.class))).thenReturn(user1);
        when(userRepository1.save(any(User.class))).thenReturn(user1);
        when(userRepository1.findByUsername(any(String.class))).thenReturn(Optional.of(user1));

        Role role = new Role();
        role.setName("SHOP-ADMIN");
        when(roleRepository.findRoleByName(any(String.class))).thenReturn(Optional.empty());

        Role role1 = new Role();
        role1.unmarshallingFromMap(role.marshallingToMap(true), true);
        role1.setId(101l);
        when(roleRepository.save(any(Role.class))).thenReturn(role1);
        when(roleRepository1.save(any(Role.class))).thenReturn(role1);
        when(roleRepository1.findRoleByName(any(String.class))).thenReturn(Optional.of(role1));

        Statement statement1 = new Statement();
        statement1.setAction(Action.Write);
        statement1.setResource("product/add");

        Statement statement2 = new Statement();
        statement2.setAction(Action.Write);
        statement2.setResource("product/update");

        Policy policy = new Policy();
        policy.setServiceName("Admin-Policy");
        when(policyRepository.findByServiceName(any(String.class))).thenReturn(Optional.empty());

        Policy policy1 = new Policy();
        policy1.unmarshallingFromMap(policy.marshallingToMap(true), true);
        policy1.setId(101l);
        when(policyRepository.save(any(Policy.class))).thenReturn(policy1);
        when(policyRepository1.save(any(Policy.class))).thenReturn(policy1);
        when(policyRepository1.findByServiceName(any(String.class))).thenReturn(Optional.of(policy1));

        //
        TaskStack stack = TaskStack.createSync(true);
        stack.push(new SaveUser(userRepository, user));
        stack.push(new SaveRole(roleRepository, role));
        stack.push(new AssignRoleToUser(userRepository1, roleRepository1, user, role));

        stack.push(new SavePolicy(policyRepository, policy));
        stack.push(new AddStatementToPolicy(policyRepository1, policy, statement1, statement2));
        stack.push(new AssignPolicyToRole(roleRepository1, policyRepository1, role, policy));

        stack.commit(true, (message, state) -> {
            System.out.println("Status: " + state.name());
            System.out.println(message.toString());
        });
    }

}
