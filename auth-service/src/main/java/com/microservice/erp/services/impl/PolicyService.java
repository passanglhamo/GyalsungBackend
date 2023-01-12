package com.microservice.erp.services.impl;

import com.infoworks.lab.rest.models.Response;
import com.microservice.erp.domain.entities.Policy;
import com.microservice.erp.domain.repositories.IPolicyRepository;
import com.microservice.erp.domain.tasks.am.SavePolicy;
import com.microservice.erp.services.definition.IPolicyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PolicyService implements IPolicyService {
    private final IPolicyRepository repository;

    @Override
    public ResponseEntity<?> savePolicy(CreatePolicyCommand command) {
        Policy policy = new Policy();
        policy.setId(command.getId());
        policy.setPolicyName(command.getPolicyName());
        policy.setType(command.getType());
        SavePolicy savePolicy = new SavePolicy(repository, policy);
        Response response = savePolicy.execute(null);
        return (response.getStatus() == 200)
                ? ResponseEntity.ok(response.getMessage())
                : ResponseEntity.status(response.getStatus()).body(response.getError());
    }

    @Override
    public ResponseEntity<List<Policy>> getAllPolicyList() {

        List<Policy> policies = repository.findAllByOrderByPolicyNameAsc();
        return ResponseEntity.ok(policies);
    }

    @Override
    public ResponseEntity<List<Policy>> getAllMappedPolicyStatementList() {

        List<Policy> policies = repository.findAllByOrderByPolicyNameAsc().stream()
                .filter(policy -> policy.getStatements().size() != 0)
                .collect(Collectors.toList());
        return ResponseEntity.ok(policies);
    }

    @Override
    public ResponseEntity<?> updatePolicy(Policy policy) {
        repository.findById(policy.getId()).ifPresent(d -> {
            d.setPolicyName(policy.getPolicyName());
            d.setType(policy.getType());
            repository.save(d);
        });
        return ResponseEntity.ok("Policy updated successfully.");
    }

}
