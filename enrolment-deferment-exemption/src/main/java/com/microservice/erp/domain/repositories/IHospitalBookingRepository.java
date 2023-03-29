package com.microservice.erp.domain.repositories;

import com.microservice.erp.domain.entities.HospitalBooking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;

@Repository
public interface IHospitalBookingRepository extends JpaRepository<HospitalBooking, BigInteger> {
    HospitalBooking findByUserId(BigInteger userId);
}
