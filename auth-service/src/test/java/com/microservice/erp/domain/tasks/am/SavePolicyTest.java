package com.microservice.erp.domain.tasks.am;

import com.infoworks.lab.beans.tasks.definition.Task;
import com.microservice.erp.domain.entities.Policy;
import com.microservice.erp.domain.entities.Statement;
import com.microservice.erp.domain.models.Action;
import com.infoworks.lab.rest.models.Message;
import com.infoworks.lab.rest.models.Response;
import com.microservice.erp.domain.repositories.IPolicyRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigInteger;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SavePolicyTest {

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Mock private IPolicyRepository repository;

    @Test
    public void execute() {
        Statement statement = new Statement();
        statement.setAction(Action.Write);
        statement.setResource("product/add");

        Policy policy = new Policy();
        policy.addStatements(statement);
        policy.setPolicyName("Can Add Product");
        when(repository.findByPolicyName(any(String.class))).thenReturn(Optional.empty());

        Policy policy2 = new Policy();
        policy2.unmarshallingFromMap(policy.marshallingToMap(true), true);
        policy2.setId(new BigInteger(String.valueOf(101l)));
        when(repository.save(any(Policy.class))).thenReturn(policy2);
        //
        Task<Message, Response> task = new SavePolicy(repository, policy);
        Response response = task.execute(null);
        //
        Assert.assertTrue(response.toString(), response.getStatus() == 200);
        System.out.println(response.toString());
    }

    @Test
    public void abort() {
    }
}