package com.microservice.erp.domain.repositories;

import com.microservice.erp.domain.entities.NoticeConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;

/**
 * @author Passang Lhamo
 */

@Repository
public interface INoticeConfigurationRepository extends JpaRepository<NoticeConfiguration, BigInteger> {
}
