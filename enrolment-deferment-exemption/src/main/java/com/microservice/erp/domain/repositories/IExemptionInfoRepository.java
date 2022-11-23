package com.microservice.erp.domain.repositories;

import com.microservice.erp.domain.entities.ExemptionInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.Collection;

/**
 * @author Rajib Kumer Ghosh
 *
 */

@Repository
public interface IExemptionInfoRepository extends JpaRepository<ExemptionInfo, BigInteger> {
    boolean existsByUserIdAndStatusIn(BigInteger userId, Collection<Character> status);

}
