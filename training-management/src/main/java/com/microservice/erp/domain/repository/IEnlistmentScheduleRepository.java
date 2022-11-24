package com.microservice.erp.domain.repository;

import com.microservice.erp.domain.entities.EnlistmentSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;


@Repository
public interface IEnlistmentScheduleRepository extends JpaRepository<EnlistmentSchedule, BigInteger> {
    @Query(value = "FROM tms_enlistment_schedule t " +
            "WHERE ((:fromDate IS NULL) OR (t.fromDate>=:fromDate) )" +
            "AND ((:status IS NULL) OR (t.status=:status) ) ")
    List<EnlistmentSchedule> getAllByFromDate(Date fromDate, String status);

    boolean existsByToDateAfter(Date fromDate);

    boolean existsByToDateAfterAndIdNot(Date fromDate,BigInteger id);

    List<EnlistmentSchedule> findAllByOrderByFromDateDesc();



}

