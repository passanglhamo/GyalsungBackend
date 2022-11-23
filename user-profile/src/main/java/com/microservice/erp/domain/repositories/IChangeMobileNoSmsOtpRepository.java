package com.microservice.erp.domain.repositories;

import com.microservice.erp.domain.entities.ChangeMobileNoSmsOtp;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigInteger;

public interface IChangeMobileNoSmsOtpRepository extends JpaRepository<ChangeMobileNoSmsOtp, BigInteger> {
}
