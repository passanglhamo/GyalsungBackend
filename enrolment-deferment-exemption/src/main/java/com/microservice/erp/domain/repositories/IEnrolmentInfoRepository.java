package com.microservice.erp.domain.repositories;

import com.microservice.erp.domain.entities.EnrolmentInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Rajib Kumer Ghosh
 */

@Repository
public interface IEnrolmentInfoRepository extends JpaRepository<EnrolmentInfo, Long> {
    EnrolmentInfo findByUserId(Long userId);
}
