package com.microservice.erp.services.impl;

import com.infoworks.lab.rest.models.Response;
import com.microservice.erp.domain.entities.Policy;
import com.microservice.erp.domain.entities.Role;
import com.microservice.erp.domain.entities.User;
import com.microservice.erp.domain.repositories.IPolicyRepository;
import com.microservice.erp.domain.repositories.IRoleRepository;
import com.microservice.erp.domain.repositories.UserRepository;
import com.microservice.erp.domain.tasks.am.AssignPolicyToRole;
import com.microservice.erp.domain.tasks.am.AssignPolicyToUser;
import com.microservice.erp.domain.tasks.am.RemovePolicyFromRole;
import com.microservice.erp.services.definition.IPolicyRoleUserMappingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PolicyRoleUserMappingService implements IPolicyRoleUserMappingService {

    private final IPolicyRepository repository;
    private final IRoleRepository roleRepository;
    private final UserRepository userRepository;

    @Override
    public ResponseEntity<?> savePolicyRoleMap(@Valid CreatePolicyRoleCommand command) {
        Role role = new Role();
        role.setId(command.getRoleId());
        Policy policy = new Policy();
        policy.setId(command.getPolicyId());
        AssignPolicyToRole assignPolicy = new AssignPolicyToRole(roleRepository, repository, role, policy);
        Response response = assignPolicy.execute(null);
        return (response.getStatus() == 200)
                ? ResponseEntity.ok(response.getMessage())
                : ResponseEntity.status(response.getStatus()).body(response.getError());
    }

    @Override
    public ResponseEntity<?> savePolicyUserMap(CreatePolicyUserCommand command) {
        User user = new User();
        user.setUserId(command.getUserId());
        Optional<Policy> opt = repository.findById(command.getPolicyId());
        Policy policy = opt.isPresent() ? opt.get() : new Policy();
        AssignPolicyToUser assignPolicy = new AssignPolicyToUser(userRepository, repository, user, policy);
        Response response = assignPolicy.execute(null);
        return (response.getStatus() == 200)
                ? ResponseEntity.ok(response.getMessage())
                : ResponseEntity.status(response.getStatus()).body(response.getError());
    }


    @Override
    public ResponseEntity<List<Policy>> getAllMappedPolicyRoleByRole(BigInteger roleId) {
        List<Policy> policies = repository.findAllByRolesOrderByPolicyNameAsc(
                roleRepository.findById(roleId).get()
        );
        return ResponseEntity.ok(policies);
    }

    @Override
    public ResponseEntity<?> removePolicyRoleMap(CreatePolicyRoleCommand command) {
        Role role = new Role();
        role.setId(command.getRoleId());
        Optional<Policy> policyOpt = repository.findById(command.getPolicyId());
        Policy policy = (policyOpt.isPresent()) ? policyOpt.get() : new Policy();
        RemovePolicyFromRole assignPolicy = new RemovePolicyFromRole(roleRepository, role, policy);
        Response response = assignPolicy.execute(null);
        return (response.getStatus() == 200)
                ? ResponseEntity.ok(response.getMessage())
                : ResponseEntity.status(response.getStatus()).body(response.getError());
    }

    @Override
    public ResponseEntity<List<Policy>> getAllMappedPolicyUserByUser(BigInteger userId) {
        List<Policy> policies = repository.findAllByUsersOrderByPolicyNameAsc(
                userRepository.findByUserId(userId).get()
        );
        return ResponseEntity.ok(policies);
    }

}
