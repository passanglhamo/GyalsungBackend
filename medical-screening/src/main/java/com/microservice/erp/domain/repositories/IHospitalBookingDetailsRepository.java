package com.microservice.erp.domain.repositories;

import com.microservice.erp.domain.entities.HospitalBookingDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

@Repository
public interface IHospitalBookingDetailsRepository extends JpaRepository<HospitalBookingDetail, BigInteger> {
    List<HospitalBookingDetail> findAllByHospitalBookingIdAndAmPm(BigInteger hospitalBookingId,Character amPm);
    HospitalBookingDetail findAllByHospitalBookingIdAndUserId(BigInteger hospitalBookingId,BigInteger userId);
    HospitalBookingDetail findByUserId(BigInteger userId);

    HospitalBookingDetail findFirstByOrderByHospitalBookingDetailIdDesc();

    Optional<HospitalBookingDetail> findByHospitalBookingDetailId(BigInteger hospitalBookingDetailId);

    void deleteByHospitalBookingDetailId(BigInteger hospitalBookingDetailId);
}
