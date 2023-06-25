package com.microservice.erp.domain.repositories;

import com.microservice.erp.domain.entities.EarlyEnlistment;
import com.microservice.erp.domain.entities.GuardianConsent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigInteger;

public interface IEarlyEnlistmentRepository extends JpaRepository<EarlyEnlistment, BigInteger> {
    EarlyEnlistment findFirstByOrderByEnlistmentIdDesc();

    EarlyEnlistment findFirstByUserIdOrderByApplicationDateDesc(BigInteger userId);
}
