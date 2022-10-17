package com.microservice.erp.domain.repositories;

import com.microservice.erp.domain.entities.NotificationInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Rajib Kumer Ghosh
 *
 */

@Repository
public interface INotificationInfoRepository extends JpaRepository<NotificationInfo, Long> {
}
