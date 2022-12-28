package com.microservice.erp.domain.repositories;

import com.microservice.erp.domain.entities.SaScreen;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigInteger;
import java.util.List;

public interface SaScreenRepository extends JpaRepository<SaScreen, BigInteger> {
    List<SaScreen> findAllByOrderByScreenNameAsc();
}
