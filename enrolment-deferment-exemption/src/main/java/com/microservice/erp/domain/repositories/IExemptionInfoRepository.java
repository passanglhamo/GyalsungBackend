package com.microservice.erp.domain.repositories;

import com.microservice.erp.domain.entities.ExemptionInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

/**
 * @author Rajib Kumer Ghosh
 *
 */

@Repository
public interface IExemptionInfoRepository extends JpaRepository<ExemptionInfo, Long> {
    boolean existsByUserIdAndStatusIn(Long userId, Collection<Character> status);

}
