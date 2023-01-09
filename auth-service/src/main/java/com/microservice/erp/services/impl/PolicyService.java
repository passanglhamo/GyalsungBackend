package com.microservice.erp.services.impl;

import com.infoworks.lab.rest.models.Response;
import com.microservice.erp.domain.entities.Policy;
import com.microservice.erp.domain.repositories.IPolicyRepository;
import com.microservice.erp.domain.repositories.IRoleRepository;
import com.microservice.erp.domain.repositories.IStatementRepository;
import com.microservice.erp.domain.repositories.UserRepository;
import com.microservice.erp.domain.tasks.am.SavePolicy;
import com.microservice.erp.services.definition.IPolicyService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PolicyService implements IPolicyService {
    private final IPolicyRepository repository;
    private final IStatementRepository statementRepository;

    @Override
    public ResponseEntity<?> savePolicy(CreatePolicyCommand command) {
        Policy policy = new Policy();
        policy.setId(command.getId());
        policy.setPolicyName(command.getPolicyName());
        policy.setVersion(0L);
        policy.addStatements(statementRepository.findById(command.getStatementId()).get());
        SavePolicy savePolicy = new SavePolicy(repository, policy);
        Response response = savePolicy.execute(null);
        return (response.getStatus() == 200)
                ? ResponseEntity.ok(response.getMessage())
                : ResponseEntity.status(response.getStatus()).body(response.getError());
    }

    @Override
    public ResponseEntity<List<Policy>> getAllPolicyList() {

        List<Policy> res = repository.findAllByOrderByPolicyNameAsc();
        return ResponseEntity.ok(res);
    }

}
