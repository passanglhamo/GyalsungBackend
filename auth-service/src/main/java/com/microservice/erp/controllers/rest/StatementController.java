package com.microservice.erp.controllers.rest;

import com.microservice.erp.domain.entities.Statement;
import com.microservice.erp.services.definition.IStatementService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/statement")
@AllArgsConstructor
public class StatementController {

    private final IStatementService statementService;

    @PostMapping
    public ResponseEntity<?> saveStatement(@Valid @RequestBody Statement statement) {
        return statementService.saveStatement(statement);
    }

    @GetMapping
    public List<Statement> getAllStatementList() {

        return statementService.getAllStatementList();
    }

    @GetMapping("/getAllStatementListByAction")
    public List<Statement> getAllStatementListByAction(String action) {

        return statementService.getAllStatementListByAction(action);
    }
}
