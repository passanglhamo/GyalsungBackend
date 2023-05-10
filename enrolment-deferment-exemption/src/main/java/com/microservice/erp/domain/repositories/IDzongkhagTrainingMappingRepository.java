package com.microservice.erp.domain.repositories;

import com.microservice.erp.domain.entities.DzongkhagTrainingMapping;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigInteger;
import java.util.List;

public interface IDzongkhagTrainingMappingRepository extends JpaRepository<DzongkhagTrainingMapping, BigInteger> {
    List<DzongkhagTrainingMapping> findAllByStatus(String status);

    boolean existsByTrainingId(Integer trainingId);

    boolean existsByTrainingIdAndIdNot(Integer trainingId, BigInteger id);
}
