package com.microservice.erp.domain.repositories;

import com.microservice.erp.domain.entities.SendNoticeInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SendNoticeInfoRepository extends JpaRepository<SendNoticeInfo, Long> {
    List<SendNoticeInfo> findByNoticeConfigurationIdAndYear(Long noticeConfigurationId, String year);
}