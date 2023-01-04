/*
package com.microservice.erp.domain.repositories;


import com.microservice.erp.domain.entities.SaRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;

@Repository
public interface ISaRoleRepository extends JpaRepository<SaRole, BigInteger> {

    List<SaRole> findAllByOrderByRoleNameAsc();

    SaRole findByIsOpenUser(Character isOpenUser);

    boolean existsByIsOpenUser(Character isOpenUser);

    boolean existsByIsOpenUserAndIdNot(Character isOpenUser, BigInteger id);
}
*/
