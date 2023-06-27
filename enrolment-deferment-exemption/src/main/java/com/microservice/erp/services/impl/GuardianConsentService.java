package com.microservice.erp.services.impl;

import com.microservice.erp.domain.dto.GuardianConsentDto;
import com.microservice.erp.domain.entities.GuardianConsent;
import com.microservice.erp.domain.helper.MessageResponse;
import com.microservice.erp.domain.repositories.IGuardianConsentRepository;
import com.microservice.erp.services.iServices.IGuardianConsentService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigInteger;

@Service
public class GuardianConsentService implements IGuardianConsentService {
    private final IGuardianConsentRepository iGuardianConsentRepository;

    public GuardianConsentService(IGuardianConsentRepository iGuardianConsentRepository) {
        this.iGuardianConsentRepository = iGuardianConsentRepository;
    }

    @Override
    public ResponseEntity<?> validateGuardianConsentLink(GuardianConsentDto guardianConsentDto) {
        BigInteger consentIdFromUrl = guardianConsentDto.getConsentIdIdFromUrl();
        String guardianCidFromUrl = guardianConsentDto.getGuardianCidFromUrl();
        GuardianConsent guardianConsentDb = iGuardianConsentRepository.findByConsentIdAndGuardianCid(consentIdFromUrl, guardianCidFromUrl);
        if (guardianConsentDb == null) {
            return ResponseEntity.badRequest().body(new MessageResponse("Data not found"));
        } else {
            return ResponseEntity.ok(guardianConsentDb);
        }
    }
}
