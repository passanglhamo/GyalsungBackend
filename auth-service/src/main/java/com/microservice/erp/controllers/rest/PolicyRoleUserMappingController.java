package com.microservice.erp.controllers.rest;

import com.microservice.erp.domain.entities.Policy;
import com.microservice.erp.services.definition.IPolicyRoleUserMappingService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.List;

@RestController
@RequestMapping("/policyRoleUser")
@AllArgsConstructor
public class PolicyRoleUserMappingController {

    private final IPolicyRoleUserMappingService iPolicyRoleUserMappingService;

    @PostMapping("/savePolicyRoleMap")
    public ResponseEntity<?> savePolicyRoleMap(@RequestBody IPolicyRoleUserMappingService.CreatePolicyRoleCommand command) {

        return iPolicyRoleUserMappingService.savePolicyRoleMap(command);
    }

    @GetMapping("/getAllMappedPolicyRoleByRole")
    public ResponseEntity<List<Policy>> getAllMappedPolicyRoleByRole(@RequestParam("roleId") BigInteger roleId) {

        return iPolicyRoleUserMappingService.getAllMappedPolicyRoleByRole(roleId);
    }

    @GetMapping("/getAllMappedPolicyUserByUser")
    public ResponseEntity<List<Policy>> getAllMappedPolicyUserByUser(@RequestParam("userId") BigInteger userId) {

        return iPolicyRoleUserMappingService.getAllMappedPolicyUserByUser(userId);
    }

    @PostMapping("/removePolicyRoleMap")
    public ResponseEntity<?> removePolicyRoleMap(@RequestBody IPolicyRoleUserMappingService.CreatePolicyRoleCommand command) {

        return iPolicyRoleUserMappingService.removePolicyRoleMap(command);
    }


    @PostMapping("/savePolicyUserMap")
    public ResponseEntity<?> savePolicyUserMap(@RequestBody IPolicyRoleUserMappingService.CreatePolicyUserCommand command) {

        return iPolicyRoleUserMappingService.savePolicyUserMap(command);
    }
}
