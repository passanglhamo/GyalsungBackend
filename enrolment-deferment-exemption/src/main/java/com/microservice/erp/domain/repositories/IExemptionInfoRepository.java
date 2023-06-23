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

    @Query(value = "select e.* FROM ede_exemption_info e \n" +
            "WHERE (e.status=:status) AND (:gender IS NULL OR CAST(e.gender AS CHAR(1))=CAST(:gender AS CHAR(1))) AND (:reasonId IS NULL OR CAST(e.reason_id AS CHAR(255)) =CAST(:reasonId AS CHAR(255)))", nativeQuery = true)
    List<ExemptionInfo> getExemptionListByToDateStatus(Character status,Character gender, BigInteger reasonId);

    @Query(value = "select e.* from ede_exemption_info e \n" +
            "where e.user_id =:userId order by exemption_id DESC LIMIT 1", nativeQuery = true)
    ExemptionInfo getExemptionByUserId(BigInteger userId);

    @Query(value = "select e.* from ede_exemption_info e \n" +
            "where e.user_id =:userId AND (e.status !=:rejectStatus) " +
            "order by exemption_id DESC LIMIT 1", nativeQuery = true)
    ExemptionInfo getExemptionByUserIdNotRejected(BigInteger userId, Character rejectStatus);

    List<ExemptionInfo> findByExemptionYearAndStatus(String year, Character status);

    List<ExemptionInfo> findAllByStatusAndUserId(Character status, BigInteger userId);

    List<ExemptionInfo> findAllByUserIdOrderByIdDesc(BigInteger userId);

    ExemptionInfo findByReasonIdAndApplicationDateOrderByIdDesc(BigInteger reasonId, Date presentDate);
}
