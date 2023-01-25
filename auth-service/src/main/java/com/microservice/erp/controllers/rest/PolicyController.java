package com.microservice.erp.controllers.rest;

import com.microservice.erp.domain.entities.Policy;
import com.microservice.erp.services.definition.IPolicyService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/policy")
@AllArgsConstructor
public class PolicyController {

    private final IPolicyService iPolicyService;

    @PostMapping
    public ResponseEntity<?> savePolicy(@Valid @RequestBody IPolicyService.CreatePolicyCommand command) {
        return iPolicyService.savePolicy(command);
    }

    @GetMapping
    public ResponseEntity<List<Policy>> getAllPolicyList() {

        return iPolicyService.getAllPolicyList();
    }

    @GetMapping("/getAllMappedPolicyStatementList")
    public ResponseEntity<List<Policy>> getAllMappedPolicyStatementList() {

        return iPolicyService.getAllMappedPolicyStatementList();
    }

    @PutMapping("/updatePolicy")
    public ResponseEntity<?> updatePolicy(@RequestHeader(HttpHeaders.AUTHORIZATION) String token,
                                          @Valid @RequestBody Policy policy) {
        return iPolicyService.updatePolicy(policy);
    }


}
