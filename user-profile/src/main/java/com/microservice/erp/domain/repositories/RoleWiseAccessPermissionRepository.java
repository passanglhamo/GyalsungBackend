package com.microservice.erp.domain.repositories;

import com.microservice.erp.domain.dto.PermissionListDto;
import com.microservice.erp.domain.entities.RoleWiseAccessPermission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigInteger;

public interface RoleWiseAccessPermissionRepository extends JpaRepository<RoleWiseAccessPermission, BigInteger> {

    RoleWiseAccessPermission findTop1ByRoleId(BigInteger roleId);
}