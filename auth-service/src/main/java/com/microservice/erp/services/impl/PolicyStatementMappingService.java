package com.microservice.erp.services.impl;

import com.infoworks.lab.rest.models.Response;
import com.microservice.erp.domain.entities.Policy;
import com.microservice.erp.domain.entities.Statement;
import com.microservice.erp.domain.models.Action;
import com.microservice.erp.domain.repositories.IPolicyRepository;
import com.microservice.erp.domain.repositories.IStatementRepository;
import com.microservice.erp.domain.tasks.am.AddStatementToPolicy;
import com.microservice.erp.services.definition.IPolicyStatementMappingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.math.BigInteger;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PolicyStatementMappingService implements IPolicyStatementMappingService {

    private final IPolicyRepository repository;
    private final IStatementRepository statementRepository;

    @Override
    public ResponseEntity<?> savePolicyStatementMapping(@Valid CreatePolicyStatementCommand command) {
        Policy policy = new Policy();
        policy.setId(command.getPolicyId());
        Statement statement = new Statement();
        statement.setId(command.getId());
        statement.setResource(command.getResource());
        statement.setAction(Action.valueOf(command.getAction()));
        Optional<Statement> exist = Objects.isNull(command.getId()) ? Optional.empty() : statementRepository.findById(command.getId());
        if (exist.isPresent()) {
            statement = exist.get();
            statement.setId(command.getId());
            statement.setResource(command.getResource());
            statement.setAction(Action.valueOf(command.getAction()));
        }

        AddStatementToPolicy addStmt = new AddStatementToPolicy(repository, policy, statement);
        Response response = addStmt.execute(null);
        return (response.getStatus() == 200)
                ? ResponseEntity.ok(response.getMessage())
                : ResponseEntity.status(response.getStatus()).body(response.getError());
    }

    @Override
    public ResponseEntity<List<Statement>> getStatementsByPolicyId(BigInteger policyId) {
        List<Statement> statements = repository.findById(policyId)
                .map(Policy::getStatements)
                .get();
        return ResponseEntity.ok(statements);
    }


}
