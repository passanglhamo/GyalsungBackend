package com.microservice.erp.domain.repositories;

import com.microservice.erp.domain.entities.GuardianConsent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigInteger;

public interface IGuardianConsentRepository extends JpaRepository<GuardianConsent, BigInteger> {
    GuardianConsent findFirstByOrderByConsentIdDesc();

    GuardianConsent findFirstByUserIdOrderByConsentRequestDateDesc(BigInteger userId);

    GuardianConsent findByConsentIdAndGuardianCid(BigInteger consentId, String guardianCid);

}
