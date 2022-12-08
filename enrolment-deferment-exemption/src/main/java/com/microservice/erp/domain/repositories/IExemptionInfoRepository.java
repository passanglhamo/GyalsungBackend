package com.microservice.erp.domain.repositories;

import com.microservice.erp.domain.entities.ExemptionInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.Collection;
import java.util.List;

@Repository
public interface IExemptionInfoRepository extends JpaRepository<ExemptionInfo, BigInteger> {
    boolean existsByUserIdAndStatusIn(BigInteger userId, Collection<Character> status);

    @Query(value = "FROM ede_exemption_info e " +
            "WHERE ((e.status=:status) )")
    List<ExemptionInfo> getExemptionListByToDateStatus(Character status);

    @Query(value = "select e.* from ede_exemption_info e \n" +
            "where e.user_id =:userId) order by exemption_id DESC LIMIT 1", nativeQuery = true)
    ExemptionInfo getExemptionByUserId(BigInteger userId);

    @Query(value = "select e.* from ede_exemption_info e \n" +
            "where e.user_id =:userId AND (e.status !=:cancelStatus) " +
            "order by exemption_id DESC LIMIT 1", nativeQuery = true)
    ExemptionInfo getExemptionByUserIdNotCancelled(BigInteger userId, Character cancelStatus);

}
