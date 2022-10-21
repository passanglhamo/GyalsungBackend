package com.microservice.erp.domain.repositories;

import com.microservice.erp.domain.entities.ParentConsentOtp;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParentConsentOtpRepository extends JpaRepository<ParentConsentOtp, Long> {

    ParentConsentOtp findByUserId(Long userId);
}
