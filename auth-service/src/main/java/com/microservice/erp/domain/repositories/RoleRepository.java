package com.microservice.erp.domain.repositories;

import com.microservice.erp.domain.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, BigInteger> {
    Optional<Role> findRoleByRoleName(String name);

    Role findByIsOpenUser(Character isOpenUser);
}
