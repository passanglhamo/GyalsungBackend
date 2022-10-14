package com.microservice.erp.domain.repositories;

import com.microservice.erp.domain.entities.Role;
import com.microservice.erp.domain.helper.ERole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(ERole name);
}
