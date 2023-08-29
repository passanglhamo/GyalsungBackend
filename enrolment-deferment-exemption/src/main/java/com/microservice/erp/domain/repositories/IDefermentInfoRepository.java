package com.microservice.erp.domain.repositories;

import com.microservice.erp.domain.entities.DefermentInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.Optional;


public interface IDefermentInfoRepository extends JpaRepository<DefermentInfo, BigInteger> {
    @Query(value = "SELECT d.* FROM ede_deferment_info d " +
            "WHERE (:defermentYear IS NULL OR (CAST(d.deferment_year AS CHAR(4)) = CAST(:defermentYear AS CHAR(4)))) AND (d.status=:status)" +
            "AND (:gender IS NULL OR CAST(d.gender AS CHAR(1))=CAST(:gender AS CHAR(1))) AND (:caseNumber IS NULL OR CAST(d.case_number AS VARCHAR (20))=CAST(:caseNumber AS VARCHAR(20))) AND (:reasonId IS NULL OR CAST(d.reason_id AS CHAR(255)) =CAST(:reasonId AS CHAR(255)))" +
            "AND (:mailStatus IS NULL OR CAST(d.mail_status AS CHAR(255)) =CAST(:mailStatus AS CHAR(255)))", nativeQuery = true)
    List<DefermentInfo> getDefermentListByToDateStatus(String defermentYear, Character status, Character gender, BigInteger reasonId, String caseNumber, Character mailStatus);

    @Query(value = "select d.* from ede_deferment_info d \n" +
            "where d.user_id =:userId order by deferment_id DESC LIMIT 1", nativeQuery = true)
    DefermentInfo getDefermentByUserId(BigInteger userId);

    List<DefermentInfo> findByDefermentYearAndStatus(String year, Character status);

    List<DefermentInfo> findAllByDefermentYearAndUserIdAndStatusIn(String defermentYear, BigInteger userId, List<Character> status);

    List<DefermentInfo> findAllByDefermentYearAndUserIdAndStatusAndMailStatus(String defermentYear, BigInteger userId,Character status,Character mailStatus);

    List<DefermentInfo> findAllByUserIdOrderByDefermentIdDesc(BigInteger userId);

    List<DefermentInfo> findAllByStatusAndUserId(Character status, BigInteger userId);

    DefermentInfo findByDefermentYearAndStatusAndUserId(String year, Character status, BigInteger userId);

    @Query(value = "select d.* from ede_deferment_info d \n" +
            "where d.reason_id = :reasonId and DATE(d.application_date) = DATE(:applicationDate) order by deferment_id DESC LIMIT 1", nativeQuery = true)
    DefermentInfo getLatestDefermentByReasonIdAndApplicationDate(BigInteger reasonId, Date applicationDate);

    List<DefermentInfo> findAllByDefermentId(List<BigInteger> defermentIds);

    DefermentInfo findFirstByOrderByDefermentIdDesc();

    Optional<DefermentInfo> findByDefermentId(BigInteger defermentId);
}
