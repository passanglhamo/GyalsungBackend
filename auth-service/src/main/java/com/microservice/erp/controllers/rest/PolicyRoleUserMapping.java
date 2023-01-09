package com.microservice.erp.controllers.rest;

import com.microservice.erp.services.definition.IPolicyRoleUserMappingService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/policyRoleUser")
@AllArgsConstructor
public class PolicyRoleUserMapping {

    private final IPolicyRoleUserMappingService iPolicyRoleUserMappingService;

    @PostMapping("/savePolicyRoleMap")
    public ResponseEntity<?> savePolicyRoleMap(@RequestBody IPolicyRoleUserMappingService.CreatePolicyRoleCommand command) {

        return iPolicyRoleUserMappingService.savePolicyRoleMap(command);
    }

    @GetMapping("/getPolicyRoleMap")
    public ResponseEntity<?> getPolicyRoleMap() {

        return iPolicyRoleUserMappingService.getPolicyRoleMap();
    }

    @PostMapping("/savePolicyUserMap")
    public ResponseEntity<?> savePolicyUserMap(IPolicyRoleUserMappingService.CreatePolicyUserCommand command) {

        return iPolicyRoleUserMappingService.savePolicyUserMap(command);
    }
}
