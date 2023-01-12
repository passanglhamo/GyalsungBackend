package com.microservice.erp.domain.repositories;

import com.microservice.erp.domain.entities.AutoExemptionFile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigInteger;
import java.util.List;

public interface IAutoExemptionFileRepository extends JpaRepository<AutoExemptionFile, BigInteger> {
    List<AutoExemptionFile> findAllByOrderByCreatedDateDesc();
}
