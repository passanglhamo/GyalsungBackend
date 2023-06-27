package com.microservice.erp.services.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.microservice.erp.domain.dto.EventBus;
import com.microservice.erp.domain.dto.GuardianConsentDto;
import com.microservice.erp.domain.entities.GuardianConsent;
import com.microservice.erp.domain.entities.GuardianConsentOtp;
import com.microservice.erp.domain.helper.MessageResponse;
import com.microservice.erp.domain.helper.OTPGenerator;
import com.microservice.erp.domain.repositories.IGuardianConsentOtpRepository;
import com.microservice.erp.domain.repositories.IGuardianConsentRepository;
import com.microservice.erp.services.iServices.IGuardianConsentService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

@Service
public class GuardianConsentService implements IGuardianConsentService {
    private final IGuardianConsentRepository iGuardianConsentRepository;
    private final IGuardianConsentOtpRepository iGuardianConsentOtpRepository;
    private final AddToQueue addToQueue;


    public GuardianConsentService(IGuardianConsentRepository iGuardianConsentRepository, IGuardianConsentOtpRepository iGuardianConsentOtpRepository, AddToQueue addToQueue) {
        this.iGuardianConsentRepository = iGuardianConsentRepository;
        this.iGuardianConsentOtpRepository = iGuardianConsentOtpRepository;
        this.addToQueue = addToQueue;
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

    @Override
    public ResponseEntity<?> validateGuardian(GuardianConsentDto guardianConsentDto) {
        BigInteger consentId = guardianConsentDto.getConsentId();
        String guardianCid = guardianConsentDto.getGuardianCid();
        GuardianConsent guardianConsentDb = iGuardianConsentRepository.findByConsentIdAndGuardianCid(consentId, guardianCid);
        if (guardianConsentDb == null) {
            return ResponseEntity.badRequest().body(new MessageResponse("Your CID or date of birth did not match the consent request data."));
        } else {
            if (guardianConsentDb.getStatus() != 'P') {
                return ResponseEntity.badRequest().body(new MessageResponse("Something went wrong. You have already consented or denied."));
            } else {
                return ResponseEntity.ok(guardianConsentDb);
            }
        }
    }

    @Override
    public ResponseEntity<?> receiveOtp(GuardianConsentDto guardianConsentDto) throws JsonProcessingException {
        GuardianConsentOtp guardianConsentOtp = new GuardianConsentOtp();

        //delete by cid and mobile number if exists
        List<GuardianConsentOtp> guardianConsentOtpDelete = iGuardianConsentOtpRepository.findAllByMobileNoAndGuardianCid(guardianConsentDto.getMobileNo(), guardianConsentDto.getGuardianCid());
        if (guardianConsentOtpDelete != null) {
            iGuardianConsentOtpRepository.deleteAll(guardianConsentOtpDelete);
        }
        GuardianConsentOtp guardianConsentOtpDb = iGuardianConsentOtpRepository.findFirstByOrderByOtpIdDesc();
        BigInteger otpId = guardianConsentOtpDb == null ? BigInteger.ONE : guardianConsentOtpDb.getOtpId().add(BigInteger.ONE);
        String otp = OTPGenerator.generateOtp();
        guardianConsentOtp.setOtpId(otpId);
        guardianConsentOtp.setGuardianCid(guardianConsentDto.getGuardianCid());
        guardianConsentOtp.setMobileNo(guardianConsentDto.getMobileNo());
        guardianConsentOtp.setOtp(otp);
        guardianConsentOtp.setDate(new Date());
        guardianConsentOtp.setExpiryTime(180);//seconds
        iGuardianConsentOtpRepository.save(guardianConsentOtp);
        String messageSms = "Your OTP for parent/guardian consent is " + otp + " Please use this within 3 minutes.";
        EventBus eventBusSms = EventBus.withId(null, null, null, messageSms, null, guardianConsentDto.getMobileNo(), null, null);
        addToQueue.addToQueue("sms", eventBusSms);
        return ResponseEntity.ok(new MessageResponse("OTP sent successfully."));
    }

    @Override
    public ResponseEntity<?> verifyOtp(GuardianConsentDto guardianConsentDto) {
        GuardianConsentOtp guardianConsentOtpDb = iGuardianConsentOtpRepository.findAllByGuardianCidAndMobileNoAndOtp(guardianConsentDto.getGuardianCid()
                , guardianConsentDto.getMobileNo(), guardianConsentDto.getOtp());
        if (guardianConsentOtpDb == null) {
            return ResponseEntity.badRequest().body(new MessageResponse("The OTP didn't match."));
        } else {
            iGuardianConsentOtpRepository.deleteById(guardianConsentOtpDb.getOtpId());
            return ResponseEntity.ok(new MessageResponse("OTP verified successfully."));
        }
    }
}
