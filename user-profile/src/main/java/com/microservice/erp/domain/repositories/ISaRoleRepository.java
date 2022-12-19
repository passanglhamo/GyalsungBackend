package com.microservice.erp.domain.repositories;



import com.microservice.erp.domain.entities.SaRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigInteger;
import java.util.List;


public interface ISaRoleRepository extends JpaRepository<SaRole, Integer> {

    List<SaRole> findAllByOrderByNameAsc();
}
