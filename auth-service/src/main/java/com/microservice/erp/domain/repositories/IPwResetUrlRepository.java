package com.microservice.erp.domain.repositories;

import com.microservice.erp.domain.entities.PwResetUrl;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigInteger;

public interface IPwResetUrlRepository extends JpaRepository<PwResetUrl, BigInteger> {

    PwResetUrl findByUserIdAndEmail(BigInteger userId, String email);

 }
