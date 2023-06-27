package com.microservice.erp.domain.repositories;

import com.microservice.erp.domain.entities.GuardianConsentOtp;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigInteger;
import java.util.List;

public interface IGuardianConsentOtpRepository extends JpaRepository<GuardianConsentOtp, BigInteger> {
    GuardianConsentOtp findFirstByOrderByOtpIdDesc();

    List<GuardianConsentOtp> findAllByMobileNoAndGuardianCid(String mobileNo, String guardianCid);

    GuardianConsentOtp findAllByGuardianCidAndMobileNoAndOtp(String guardianCid, String mobileNo, String otp);
}
