package com.microservice.erp.domain.repositories;

import com.microservice.erp.domain.entities.HospitalScheduleTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.math.BigInteger;

@Repository
public interface IHospitalScheduleTimeRepository extends JpaRepository<HospitalScheduleTime, BigInteger> {
    HospitalScheduleTime findByBookedBy(BigInteger userId);
}
