package com.microservice.erp.domain.repositories;

import com.microservice.erp.domain.entities.ParentConsent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigInteger;
import java.util.List;

public interface ParentConsentRepository extends JpaRepository<ParentConsent, BigInteger> {
    ParentConsent findByUserId(BigInteger userId);

    boolean existsByUserId(BigInteger userId);

    List<ParentConsent> findByYearOrderBySubmittedOnAsc(String year);
}
