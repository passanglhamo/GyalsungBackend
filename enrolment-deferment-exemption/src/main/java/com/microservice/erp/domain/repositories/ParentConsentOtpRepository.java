package com.microservice.erp.domain.repositories;

import com.microservice.erp.domain.entities.ParentConsentOtp;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigInteger;

public interface ParentConsentOtpRepository extends JpaRepository<ParentConsentOtp, BigInteger> {

    ParentConsentOtp findByUserId(BigInteger userId);
}
