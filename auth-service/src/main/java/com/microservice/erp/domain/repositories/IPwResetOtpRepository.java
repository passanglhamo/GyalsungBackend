package com.microservice.erp.domain.repositories;

import com.microservice.erp.domain.entities.PwResetOtp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;

@Repository
public interface IPwResetOtpRepository extends JpaRepository<PwResetOtp, BigInteger> {
    PwResetOtp findByUserId(BigInteger userId);

    PwResetOtp findByUserIdAndMobileNoAndOtp(BigInteger userId, String enteredMobileNumber, String opt);

}
