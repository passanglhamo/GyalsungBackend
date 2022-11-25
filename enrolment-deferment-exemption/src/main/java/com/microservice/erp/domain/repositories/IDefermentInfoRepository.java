package com.microservice.erp.domain.repositories;

import com.microservice.erp.domain.entities.DefermentInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigInteger;
import java.util.Collection;
import java.util.Date;
import java.util.List;


public interface IDefermentInfoRepository extends JpaRepository<DefermentInfo, BigInteger> {

 boolean existsByUserIdAndStatusInAndToDateGreaterThanEqual(BigInteger userId, Collection<Character> status,
                                                            Date toDate);

 @Query(value = "select d.* from ede_deferment_info d \n" +
         "where d.to_date <=:toDate AND d.status =:status", nativeQuery = true)
 List<DefermentInfo> getDefermentListByToDateStatus(Date toDate, Character status);

}
