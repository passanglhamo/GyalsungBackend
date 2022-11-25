package com.microservice.erp.domain.repositories;

import com.microservice.erp.domain.entities.ExemptionInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Repository
public interface IExemptionInfoRepository extends JpaRepository<ExemptionInfo, BigInteger> {
    boolean existsByUserIdAndStatusIn(BigInteger userId, Collection<Character> status);

    @Query(value = "FROM ede_exemption_info e " +
            "WHERE ((:status IS NULL) OR (e.status=:status) )" )
    List<ExemptionInfo> getExemptionListByToDateStatus(String status);

}
