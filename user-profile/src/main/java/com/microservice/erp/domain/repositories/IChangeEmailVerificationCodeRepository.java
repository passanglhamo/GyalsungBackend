package com.microservice.erp.domain.repositories;

import com.microservice.erp.domain.entities.ChangeEmailVerificationCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigInteger;

public interface IChangeEmailVerificationCodeRepository extends JpaRepository<ChangeEmailVerificationCode, BigInteger> {
}
