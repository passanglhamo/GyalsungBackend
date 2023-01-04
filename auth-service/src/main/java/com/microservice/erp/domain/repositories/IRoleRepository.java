package com.microservice.erp.domain.repositories;

import com.microservice.erp.domain.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;

@Repository
public interface IRoleRepository extends JpaRepository<Role, BigInteger> {

    List<Role> findAllByOrderByRoleNameAsc();

    Role findByIsOpenUser(Character isOpenUser);

    boolean existsByIsOpenUser(Character isOpenUser);

    boolean existsByIsOpenUserAndIdNot(Character isOpenUser, BigInteger id);
}

