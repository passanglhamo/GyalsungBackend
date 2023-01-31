package com.microservice.erp.domain.repositories;

import com.microservice.erp.domain.entities.RoleWiseAccessPermission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigInteger;
import java.util.Optional;

public interface RoleWiseAccessPermissionRepository extends JpaRepository<RoleWiseAccessPermission, BigInteger> {

    RoleWiseAccessPermission findTop1ByRoleId(BigInteger roleId);
    Optional<RoleWiseAccessPermission> findByScreenId(BigInteger screenId);
}
