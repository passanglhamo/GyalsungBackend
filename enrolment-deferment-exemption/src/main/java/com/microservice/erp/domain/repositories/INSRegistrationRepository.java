package com.microservice.erp.domain.repositories;

import com.microservice.erp.domain.entities.NSRegistration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigInteger;
import java.util.List;

public interface INSRegistrationRepository extends JpaRepository<NSRegistration, BigInteger> {
    NSRegistration findByUserId(BigInteger bigInteger);
    @Query(value = "SELECT r.* FROM ede_ns_registration r " +
            "WHERE (:enlistmentYear IS NULL OR (CAST(r.year AS CHAR(4)) = CAST(:enlistmentYear AS CHAR(4)))) AND (r.status=:status)" +
            "AND (:gender IS NULL OR CAST(r.gender AS CHAR(1))=CAST(:gender AS CHAR(1)))", nativeQuery = true)
    List<NSRegistration> getRegistrationListByStatus(String enlistmentYear, Character status, Character gender);
}
