package com.microservice.erp.domain.repository;

import com.microservice.erp.domain.entities.Reason;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;

@Repository
public interface IReasonRepository extends JpaRepository<Reason, BigInteger> {

    List<Reason> findAllByStatus(String status);

    List<Reason> findAllByOrderByReasonNameAsc();

}
