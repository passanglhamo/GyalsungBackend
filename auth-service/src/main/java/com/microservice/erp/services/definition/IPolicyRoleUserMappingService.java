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

public interface IPolicyRoleUserMappingService {
    ResponseEntity<?> savePolicyRoleMap(@Valid CreatePolicyRoleCommand command);

    ResponseEntity<?> savePolicyUserMap( CreatePolicyUserCommand command);

    ResponseEntity<List<Policy>> getAllMappedPolicyRoleByRole(BigInteger roleId);

    ResponseEntity<?> removePolicyRoleMap(CreatePolicyRoleCommand command);

    ResponseEntity<List<Policy>> getAllMappedPolicyUserByUser(BigInteger userId);

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    class CreatePolicyRoleCommand {
        private BigInteger id;
        @NotNull(message = "Role cannot be null")
        private BigInteger roleId;
        @NotNull(message = "Policy cannot be null")
        private BigInteger policyId;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    class CreatePolicyUserCommand {
        private BigInteger id;
        @NotNull(message = "User cannot be null")
        private BigInteger userId;
        @NotNull(message = "Policy cannot be null")
        private BigInteger policyId;
    }

}
