package com.microservice.erp.domain.repositories;

import com.microservice.erp.domain.entities.ScreenGroup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

public interface ScreenGroupRepository extends JpaRepository<ScreenGroup, BigInteger> {
    List<ScreenGroup> findAllByOrderByScreenGroupNameAsc();

    Optional<ScreenGroup> findByScreenGroupName(String screenGroupName);
}
