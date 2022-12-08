package com.microservice.erp.domain.repositories;

import com.microservice.erp.domain.entities.DefermentFileInfo;
import com.microservice.erp.domain.entities.DefermentInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigInteger;
import java.util.Collection;
import java.util.Date;
import java.util.List;


public interface IDefermentInfoRepository extends JpaRepository<DefermentInfo, BigInteger> {

 @Query(value = "select d.* from ede_deferment_info d \n" +
         "where d.to_date <=:toDate AND d.status =:status", nativeQuery = true)
 List<DefermentInfo> getDefermentListByToDateStatus(Date toDate, Character status);

 @Query(value = "select d.* from ede_deferment_info d \n" +
         "where d.user_id =:userId order by deferment_id DESC LIMIT 1", nativeQuery = true)
 DefermentInfo getDefermentByUserId(BigInteger userId);

 @Query(value = "select d.* from ede_deferment_info d \n" +
         "where d.user_id =:userId AND (d.status !=:cancelStatus) " +
         "order by deferment_id DESC LIMIT 1", nativeQuery = true)
 DefermentInfo getDefermentByUserIdNotCancelled(BigInteger userId,Character cancelStatus);

}
