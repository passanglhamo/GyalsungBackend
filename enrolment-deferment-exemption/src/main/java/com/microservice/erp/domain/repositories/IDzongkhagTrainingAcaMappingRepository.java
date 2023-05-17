package com.microservice.erp.domain.repositories;

import com.microservice.erp.domain.entities.DzongkhagTrainingPreAcaMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
@Repository
public interface IDzongkhagTrainingAcaMappingRepository extends JpaRepository<DzongkhagTrainingPreAcaMapping, BigInteger> {

    DzongkhagTrainingPreAcaMapping findByDzongkhagId(Integer dzongkhagId);

}
