package com.microservice.erp.domain.repositories;

import com.microservice.erp.domain.entities.AutoExemption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.math.BigInteger;
import java.util.List;

//public interface IAutoExemptionRepository extends JpaRepository<AutoExemption, BigInteger> {
public interface IAutoExemptionRepository extends PagingAndSortingRepository<AutoExemption, BigInteger> {

    List<AutoExemption> findTop50ByOrderByCreatedDateDescFullNameAsc();

    AutoExemption findByCid(String cid);
}
