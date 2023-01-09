package com.microservice.erp.services.definition;

import com.microservice.erp.domain.entities.Policy;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.ResponseEntity;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.math.BigInteger;
import java.util.List;

public interface IPolicyService {

    ResponseEntity<?> savePolicy(@Valid CreatePolicyCommand command);

    ResponseEntity<List<Policy>> getAllPolicyList();


    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    class CreatePolicyCommand {
        private BigInteger id;
        @NotNull(message = "Policy cannot be null")
        private String policyName;
        @NotNull(message = "Statement cannot be null")
        private BigInteger statementId;
    }


}
