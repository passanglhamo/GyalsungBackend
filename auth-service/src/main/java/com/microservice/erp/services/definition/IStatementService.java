package com.microservice.erp.services.definition;

import com.microservice.erp.domain.entities.Statement;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface IStatementService {
    List<Statement> getAllStatementList();

    List<Statement> getAllStatementListByAction(String action);

    ResponseEntity<?> saveStatement(Statement statement);
}
