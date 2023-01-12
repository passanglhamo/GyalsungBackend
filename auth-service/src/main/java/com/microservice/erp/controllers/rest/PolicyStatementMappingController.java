package com.microservice.erp.controllers.rest;

import com.microservice.erp.domain.entities.Statement;
import com.microservice.erp.services.definition.IPolicyStatementMappingService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.List;

@RestController
@RequestMapping("/policyStatementMapping")
@AllArgsConstructor
public class PolicyStatementMappingController {

    private final IPolicyStatementMappingService policyStatementMappingService;

    @PostMapping
    public ResponseEntity<?> savePolicyStatementMapping(@RequestBody IPolicyStatementMappingService.CreatePolicyStatementCommand command) {

        return policyStatementMappingService.savePolicyStatementMapping(command);
    }

    @GetMapping("/getStatementsByPolicyId")
    public ResponseEntity<List<Statement>> getStatementsByPolicyId(@RequestParam("policyId") BigInteger policyId) {

        return policyStatementMappingService.getStatementsByPolicyId(policyId);
    }
}
