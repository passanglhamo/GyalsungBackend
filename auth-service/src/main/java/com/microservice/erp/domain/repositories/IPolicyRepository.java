package com.microservice.erp.domain.repositories;

import com.microservice.erp.domain.entities.Policy;
import com.microservice.erp.domain.entities.Role;
import com.microservice.erp.domain.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

@Repository
public interface IPolicyRepository extends JpaRepository<Policy, BigInteger> {
    List<Policy> findAllByOrderByPolicyNameAsc();

    List<Policy> findAllByRolesOrderByPolicyNameAsc(Role role);

    List<Policy> findAllByUsersOrderByPolicyNameAsc(User user);

    Optional<Policy> findByPolicyName(String policyName);

    List<Policy> findAllByRoles(List<Role> role);
}
