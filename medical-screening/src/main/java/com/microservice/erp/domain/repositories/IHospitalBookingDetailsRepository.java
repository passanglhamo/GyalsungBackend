package com.microservice.erp.domain.repositories;

import com.microservice.erp.domain.entities.HospitalBookingDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;

@Repository
public interface IHospitalBookingDetailsRepository extends JpaRepository<HospitalBookingDetail, BigInteger> {
    List<HospitalBookingDetail> findAllByHospitalBookingIdAndAmPm(BigInteger hospitalBookingId,Character amPm);
    HospitalBookingDetail findAllByHospitalBookingIdAndUserId(BigInteger hospitalBookingId,BigInteger userId);
    HospitalBookingDetail findByUserId(BigInteger userId);
}
