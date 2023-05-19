package com.microservice.erp.domain.repositories;

import com.microservice.erp.domain.entities.AgeCriteria;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigInteger;

public interface IAgeCriteriaRepository extends JpaRepository<AgeCriteria, BigInteger> {
    AgeCriteria findTopByOrderByMinimumAgeDesc();
}
