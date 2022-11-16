package com.microservice.erp.domain.repositories;

import com.microservice.erp.domain.entities.SendNoticeInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SendNoticeInfoRepository extends JpaRepository<SendNoticeInfo, Long> {
}
