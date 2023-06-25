package com.microservice.erp.domain.repositories;

import com.microservice.erp.domain.entities.NSRegistration;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigInteger;

public interface INSRegistrationRepository extends JpaRepository<NSRegistration, BigInteger> {
    NSRegistration findByUserId(BigInteger bigInteger);
}
