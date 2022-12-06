package com.microservice.erp.domain.repositories;

import com.microservice.erp.domain.entities.SaRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigInteger;
import java.util.List;

public interface ISaRoleRepository extends JpaRepository<SaRole, BigInteger> {
    SaRole findByRoleId(Integer roleId);

    List<SaRole> findAllByOrderByRoleNameAsc();
}