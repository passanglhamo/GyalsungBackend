package com.microservice.erp.domain.repositories;

import com.microservice.erp.domain.entities.HospitalBookingDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;

@Repository
public interface IHospitalBookingDetailsRepository extends JpaRepository<HospitalBookingDetail, BigInteger> {
}