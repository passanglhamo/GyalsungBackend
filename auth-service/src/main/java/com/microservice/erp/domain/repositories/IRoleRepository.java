package com.microservice.erp.domain.repositories;

import com.microservice.erp.domain.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

@Repository
public interface IRoleRepository extends JpaRepository<Role, BigInteger> {

    List<Role> findAllByOrderByRoleNameAsc();

    Role findByIsOpenUser(Character isOpenUser);

    Optional<Role> findByRoleName(String roleName);

    boolean existsByIsOpenUser(Character isOpenUser);

    boolean existsByIsOpenUserAndIdNot(Character isOpenUser, BigInteger id);
}

