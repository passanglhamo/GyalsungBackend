package com.microservice.erp.domain.repositories;

import com.microservice.erp.domain.entities.AutoExemption;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigInteger;
import java.util.List;

public interface IAutoExemptionRepository extends JpaRepository<AutoExemption, BigInteger> {

    List<AutoExemption> findTop50ByOrderByCreatedDateDescFullNameAsc();

}
