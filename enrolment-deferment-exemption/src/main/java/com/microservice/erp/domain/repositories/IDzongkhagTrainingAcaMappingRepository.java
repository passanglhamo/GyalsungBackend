package com.microservice.erp.domain.repositories;

import com.microservice.erp.domain.entities.DzongkhagTrainingPreAcaMapping;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigInteger;

public interface IDzongkhagTrainingAcaMappingRepository extends JpaRepository<DzongkhagTrainingPreAcaMapping, BigInteger> {
}
