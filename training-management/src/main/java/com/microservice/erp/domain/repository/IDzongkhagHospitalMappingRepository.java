package com.microservice.erp.domain.repository;

import com.microservice.erp.domain.entities.DzongkhagHospitalMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;

/**
 * @author Passang Lhamo
 */

@Repository
public interface IDzongkhagHospitalMappingRepository extends JpaRepository<DzongkhagHospitalMapping, BigInteger> {
    DzongkhagHospitalMapping findByDzongkhagId(Integer id);

    List<DzongkhagHospitalMapping> findAllByStatus(String status);

    boolean  existsByDzongkhagIdAndHospitalId(Integer dzongkhagId,Integer hospitalId);

}
