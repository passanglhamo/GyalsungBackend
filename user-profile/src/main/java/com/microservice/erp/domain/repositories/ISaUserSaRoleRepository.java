package com.microservice.erp.domain.repositories;

import com.microservice.erp.domain.entities.SaUserRoleMapping;
import com.microservice.erp.domain.entities.SaUserSaRoleKey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigInteger;

public interface ISaUserSaRoleRepository extends JpaRepository<SaUserRoleMapping, BigInteger> {
    boolean existsById(SaUserSaRoleKey saUserSaRoleKey);
}
