package com.microservice.erp.services.definition;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.ResponseEntity;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.math.BigInteger;

public interface IPolicyRoleUserMappingService {
    ResponseEntity<?> savePolicyRoleMap(@Valid CreatePolicyRoleCommand command);

    ResponseEntity<?> savePolicyUserMap( CreatePolicyUserCommand command);

    ResponseEntity<?> getPolicyRoleMap();

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
        private String userName;
        @NotNull(message = "Policy cannot be null")
        private BigInteger policyId;
    }

}
