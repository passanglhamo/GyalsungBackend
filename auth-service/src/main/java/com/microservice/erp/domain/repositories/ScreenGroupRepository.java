package com.microservice.erp.domain.repositories;

import com.microservice.erp.domain.entities.ScreenGroup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigInteger;
import java.util.List;

public interface ScreenGroupRepository extends JpaRepository<ScreenGroup, BigInteger> {
    List<ScreenGroup> findAllByOrderByScreenGroupNameAsc();
}
