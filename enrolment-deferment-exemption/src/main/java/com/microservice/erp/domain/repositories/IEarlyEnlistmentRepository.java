package com.microservice.erp.domain.repositories;

import com.microservice.erp.domain.entities.EarlyEnlistment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigInteger;
import java.util.List;

public interface IEarlyEnlistmentRepository extends JpaRepository<EarlyEnlistment, BigInteger> {
    EarlyEnlistment findFirstByOrderByEnlistmentIdDesc();

    EarlyEnlistment findFirstByUserIdOrderByApplicationDateDesc(BigInteger userId);
    @Query(value = "SELECT e.* FROM ede_early_enlistment e " +
            "WHERE (:enlistmentYear IS NULL OR (CAST(e.enlistment_year AS CHAR(4)) = CAST(:enlistmentYear AS CHAR(4)))) AND  (:status IS NULL OR (CAST(e.status AS CHAR(1)) = CAST(:status AS CHAR(1))))", nativeQuery = true)
    List<EarlyEnlistment> getEarlyEnlistmentByEnlistmentYearAndStatus(String enlistmentYear, Character status);
}
