package com.microservice.erp.domain.repositories;

import com.microservice.erp.domain.entities.Screen;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigInteger;
import java.util.List;

public interface ScreenRepository extends JpaRepository<Screen, BigInteger> {
    List<Screen> findAllByOrderByScreenNameAsc();
}
