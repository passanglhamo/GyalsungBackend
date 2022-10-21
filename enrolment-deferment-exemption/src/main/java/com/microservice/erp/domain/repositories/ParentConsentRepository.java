package com.microservice.erp.domain.repositories;

import com.microservice.erp.domain.entities.ParentConsent;
import org.springframework.data.jpa.repository.JpaRepository;

 public interface ParentConsentRepository extends JpaRepository<ParentConsent, Long> {
  ParentConsent findByUserId(Long userId);
 }
