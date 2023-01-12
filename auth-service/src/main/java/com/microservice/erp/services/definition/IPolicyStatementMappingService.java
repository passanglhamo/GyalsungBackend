package com.microservice.erp.services.definition;

import com.microservice.erp.domain.entities.Statement;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.ResponseEntity;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.math.BigInteger;
import java.util.List;

public interface IPolicyStatementMappingService {

    ResponseEntity<?> savePolicyStatementMapping(@Valid CreatePolicyStatementCommand command);

    ResponseEntity<List<Statement>> getStatementsByPolicyId(BigInteger policyId);

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    class CreatePolicyStatementCommand {
        private BigInteger id;
        @NotNull(message = "Policy cannot be null")
        private BigInteger policyId;
        @NotNull(message = "Resource cannot be null")
        private String resource;
        @NotNull(message = "Action cannot be null")
        private String action;
    }

}
