package com.microservice.erp.services.impl;

import com.microservice.erp.domain.entities.Statement;
import com.microservice.erp.domain.models.Action;
import com.microservice.erp.domain.repositories.IStatementRepository;
import com.microservice.erp.services.definition.IStatementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StatementService implements IStatementService {

    private final IStatementRepository repository;

    @Override
    public List<Statement> getAllStatementList() {
        return repository.findAll();
    }

    @Override
    public List<Statement> getAllStatementListByAction(String action) {
        return repository.findAllByAction(Action.valueOf(action));
    }

    @Override
    public ResponseEntity<?> saveStatement(Statement statement) {
        repository.save(statement);
        return ResponseEntity.ok("Data saved successfully.");
    }
}
