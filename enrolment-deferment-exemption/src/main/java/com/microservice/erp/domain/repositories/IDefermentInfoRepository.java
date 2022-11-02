package com.microservice.erp.domain.repositories;

import com.microservice.erp.domain.entities.DefermentInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Date;

/**
 * @author Rajib Kumer Ghosh
 */


public interface IDefermentInfoRepository extends JpaRepository<DefermentInfo, Long> {

 boolean  existsByUserIdAndStatusInAndToDateGreaterThanEqual(Long userId, Collection<Character> status,
                                                                         Date toDate);

}