package com.microservice.erp.domain.repositories;

import com.microservice.erp.domain.entities.PersonStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigInteger;
import java.util.List;

public interface IPersonStatusRepository extends JpaRepository<PersonStatus, BigInteger> {

    List<PersonStatus> findAllByOrderByPersonStatusAsc();

}
