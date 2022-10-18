package com.microservice.erp.domain.repository;

import com.microservice.erp.domain.entities.NoticeConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;
/**
 * @author Passang Lhamo
 *
 */
@CrossOrigin(origins = "*")
@Repository
public interface INoticeConfigurationRepository extends JpaRepository<NoticeConfiguration,Long> {
}
