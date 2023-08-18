package com.microservice.erp.domain.repositories;

import com.microservice.erp.domain.entities.DefermentInfoAudit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigInteger;
import java.util.List;

public interface IDefermentInfoAuditRepository extends JpaRepository<DefermentInfoAudit, BigInteger> {
    DefermentInfoAudit findFirstByOrderByDefermentAuditIdDesc();

    List<DefermentInfoAudit> findAllByDefermentIdOrderByDefermentAuditIdDesc(BigInteger defermentId);
}
