package com.microservice.erp.domain.repositories;

import com.microservice.erp.domain.entities.DefermentFileInfo;
import com.microservice.erp.domain.entities.DefermentInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigInteger;
import java.util.Collection;
import java.util.Date;
import java.util.List;


public interface IDefermentInfoRepository extends JpaRepository<DefermentInfo, BigInteger> {
 @Query(value = "SELECT d.* FROM ede_deferment_info d " +
         "WHERE (:defermentYear IS NULL OR (CAST(d.deferment_year AS CHAR(4)) = CAST(:defermentYear AS CHAR(4)))) AND (d.status=:status)" +
         "AND (:gender IS NULL OR CAST(d.gender AS CHAR(1))=CAST(:gender AS CHAR(1))) AND (:reasonId IS NULL OR CAST(d.reason_id AS CHAR(255)) =CAST(:reasonId AS CHAR(255)))", nativeQuery = true)
 List<DefermentInfo> getDefermentListByToDateStatus(String defermentYear,Character status,Character gender, BigInteger reasonId);

 @Query(value = "select d.* from ede_deferment_info d \n" +
         "where d.user_id =:userId order by deferment_id DESC LIMIT 1", nativeQuery = true)
 DefermentInfo getDefermentByUserId(BigInteger userId);

 @Query(value = "select d.* from ede_deferment_info d \n" +
         "where d.user_id =:userId AND (d.status !=:rejectStatus) " +
         "order by deferment_id DESC LIMIT 1", nativeQuery = true)
 DefermentInfo getDefermentByUserIdNotRejected(BigInteger userId,Character rejectStatus);

    List<DefermentInfo> findByDefermentYearAndStatus(String year, Character status);

    List<DefermentInfo> findAllByDefermentYearAndUserIdAndStatusIn(String defermentYear, BigInteger userId, List<Character> status);

    List<DefermentInfo> findAllByUserIdOrderByIdDesc(BigInteger userId);

    List<DefermentInfo> findAllByStatusAndUserId(Character status, BigInteger userId);

    DefermentInfo findByDefermentYearAndStatusAndUserId(String year, Character status,BigInteger userId);



}
