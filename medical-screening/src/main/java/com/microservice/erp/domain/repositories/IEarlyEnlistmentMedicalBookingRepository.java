package com.microservice.erp.domain.repositories;

import com.microservice.erp.domain.entities.EarlyEnlistmentMedicalBooking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigInteger;

public interface IEarlyEnlistmentMedicalBookingRepository extends JpaRepository<EarlyEnlistmentMedicalBooking, BigInteger> {
    EarlyEnlistmentMedicalBooking findFirstByOrderByHospitalBookingIdDesc();
    EarlyEnlistmentMedicalBooking findByEarlyEnlistmentIdAndUserId(BigInteger earlyEnlistmentId, BigInteger userId);
}
