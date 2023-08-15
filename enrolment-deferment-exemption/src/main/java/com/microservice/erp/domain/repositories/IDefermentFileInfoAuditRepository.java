package com.microservice.erp.domain.repositories;

import com.microservice.erp.domain.entities.DefermentFileInfoAudit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigInteger;

public interface IDefermentFileInfoAuditRepository extends JpaRepository<DefermentFileInfoAudit, BigInteger> {

    DefermentFileInfoAudit findFirstByOrderByDefermentFileAuditIdDesc();

}
