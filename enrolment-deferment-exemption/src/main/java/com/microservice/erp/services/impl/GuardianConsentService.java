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
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

@Service
public class GuardianConsentService implements IGuardianConsentService {
    @Autowired
    @Qualifier("userProfileTemplate")
    RestTemplate userRestTemplate;

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

    @Override
    public ResponseEntity<?> grantGuardianConsent(GuardianConsentDto guardianConsentDto) throws JsonProcessingException {
        BigInteger consentId = guardianConsentDto.getConsentId();
        String guardianCid = guardianConsentDto.getGuardianCid();
        GuardianConsent guardianConsentDb = iGuardianConsentRepository.findByConsentIdAndGuardianCid(consentId, guardianCid);
        if (guardianConsentDb == null) {
            return ResponseEntity.badRequest().body(new MessageResponse("Something went wrong."));
        } else {
            if (guardianConsentDb.getStatus() != 'P') {
                return ResponseEntity.badRequest().body(new MessageResponse("Something went wrong. You have already consented or denied."));
            } else {
                String fullName = guardianConsentDb.getFullName();
                String childMobileNo = guardianConsentDb.getMobileNo();
                String childEmail = guardianConsentDb.getEmail();
                String guardianName = guardianConsentDb.getGuardianName();
                String guardianMobileNo = guardianConsentDb.getGuardianMobileNo();
                String guardianEmail = guardianConsentDb.getGuardianEmail();

                GuardianConsent guardianConsent = new ModelMapper().map(guardianConsentDb, GuardianConsent.class);
                guardianConsent.setStatus('A');
                guardianConsent.setConsentDate(new Date());
                iGuardianConsentRepository.save(guardianConsent);

                //send email and sms to guardian
                String subject = "Support of Guardian Consent for Early Enlistment";
                String messageEmailGuardian = "Dear guardian of " + fullName + ",<br></br>" +
                        " You have supported your ward application for early enlistment.<br></br><br></br>" +
                        "<small>***This is a system-generated email. Please do not respond to this email.***</small>";

                String messageSmsGuardian = "Dear guardian of " + fullName + ",  You have supported your ward application for early enlistment. Thank you.";

                EventBus eventBusEmailGuardian = EventBus.withId(guardianEmail, null, null, messageEmailGuardian, subject, guardianMobileNo, null, null);
                EventBus eventBusSmsGuardian = EventBus.withId(null, null, null, messageSmsGuardian, null, guardianMobileNo, null, null);
                addToQueue.addToQueue("email", eventBusEmailGuardian);
                addToQueue.addToQueue("sms", eventBusSmsGuardian);

                //send email and sms to child
                String messageEmailChild = "Dear " + fullName + ",<br></br>" +
                        " Your primary guardian " + guardianName + " have supported your request for early enlistment.<br></br><br></br>" +
                        "<small>***This is a system-generated email. Please do not respond to this email.***</small>";

                String messageSmsChild = "Dear " + fullName +
                        ", Your primary guardian " + guardianName + " have supported your request for early enlistment. Thank you.";

                EventBus eventBusEmailChild = EventBus.withId(childEmail, null, null, messageEmailChild, subject, childMobileNo, null, null);
                EventBus eventBusSmsChild = EventBus.withId(null, null, null, messageSmsChild, null, childMobileNo, null, null);

                addToQueue.addToQueue("email", eventBusEmailChild);
                addToQueue.addToQueue("sms", eventBusSmsChild);

                return ResponseEntity.ok(new MessageResponse("Guardian consented successfully."));
            }
        }
    }

    @Override
    public ResponseEntity<?> denyGuardianConsent(GuardianConsentDto guardianConsentDto) throws JsonProcessingException {
        BigInteger consentId = guardianConsentDto.getConsentId();
        String guardianCid = guardianConsentDto.getGuardianCid();
        GuardianConsent guardianConsentDb = iGuardianConsentRepository.findByConsentIdAndGuardianCid(consentId, guardianCid);
        if (guardianConsentDb == null) {
            return ResponseEntity.badRequest().body(new MessageResponse("Something went wrong."));
        } else {
            if (guardianConsentDb.getStatus() != 'P') {
                return ResponseEntity.badRequest().body(new MessageResponse("Something went wrong. You have already consented or denied."));
            } else {
                String fullName = guardianConsentDb.getFullName();
                String childMobileNo = guardianConsentDb.getMobileNo();
                String childEmail = guardianConsentDb.getEmail();
                String guardianName = guardianConsentDb.getGuardianName();
                String guardianMobileNo = guardianConsentDb.getGuardianMobileNo();
                String guardianEmail = guardianConsentDb.getGuardianEmail();

                GuardianConsent guardianConsent = new ModelMapper().map(guardianConsentDb, GuardianConsent.class);
                guardianConsent.setStatus('R');
                guardianConsent.setConsentDate(new Date());
                iGuardianConsentRepository.save(guardianConsent);

                //send email and sms to guardian
                String subject = "Guardian Consent for Early Enlistment Not Supported";
                String messageEmailGuardian = "Dear guardian of " + fullName + ",<br></br>" +
                        " You have not supported your ward application for early enlistment.<br></br><br></br>" +
                        "<small>***This is a system-generated email. Please do not respond to this email.***</small>";

                String messageSmsGuardian = "Dear guardian of " + fullName + ",  You have not supported your ward application for early enlistment. Thank you.";

                EventBus eventBusEmailGuardian = EventBus.withId(guardianEmail, null, null, messageEmailGuardian, subject, guardianMobileNo, null, null);
                EventBus eventBusSmsGuardian = EventBus.withId(null, null, null, messageSmsGuardian, null, guardianMobileNo, null, null);
                addToQueue.addToQueue("email", eventBusEmailGuardian);
                addToQueue.addToQueue("sms", eventBusSmsGuardian);

                //send email and sms to child
                String messageEmailChild = "Dear " + fullName + ",<br></br>" +
                        " Your primary guardian " + guardianName + " have not supported your request for early enlistment.<br></br><br></br>" +
                        "<small>***This is a system-generated email. Please do not respond to this email.***</small>";

                String messageSmsChild = "Dear " + fullName +
                        ", Your primary guardian " + guardianName + " have not supported your request for early enlistment. Thank you.";

                EventBus eventBusEmailChild = EventBus.withId(childEmail, null, null, messageEmailChild, subject, childMobileNo, null, null);
                EventBus eventBusSmsChild = EventBus.withId(null, null, null, messageSmsChild, null, childMobileNo, null, null);

                addToQueue.addToQueue("email", eventBusEmailChild);
                addToQueue.addToQueue("sms", eventBusSmsChild);

                return ResponseEntity.ok(new MessageResponse("Guardian not supported."));
            }
        }
    }
}
