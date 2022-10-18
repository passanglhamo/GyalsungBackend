package com.microservice.erp.domain.repository;

import com.microservice.erp.domain.entities.EnlistmentSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.Date;
import java.util.List;

/**
 * @author Rajib Kumer Ghosh
 */
@CrossOrigin(origins = "*")
@Repository
public interface IEnlistmentScheduleRepository extends JpaRepository<EnlistmentSchedule, Long> {
    @Query(value = "FROM tms_enlistment_schedule t " +
            "WHERE ((:fromDate IS NULL) OR (t.fromDate>=:fromDate) )" +
            "AND ((:status IS NULL) OR (t.status=:status) ) ")
    List<EnlistmentSchedule> getAllByFromDate(Date fromDate,String status);

    boolean existsByToDateAfter(Date fromDate);
}
