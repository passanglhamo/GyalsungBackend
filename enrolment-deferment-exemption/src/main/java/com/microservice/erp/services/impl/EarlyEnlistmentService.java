package com.microservice.erp.services.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.microservice.erp.domain.dto.*;
import com.microservice.erp.domain.entities.EarlyEnlistment;
import com.microservice.erp.domain.entities.GuardianConsent;
import com.microservice.erp.domain.helper.*;
import com.microservice.erp.domain.mapper.EarlyEnlistmentMapper;
import com.microservice.erp.domain.repositories.IEarlyEnlistmentRepository;
import com.microservice.erp.domain.repositories.IGuardianConsentRepository;
import com.microservice.erp.services.iServices.IEarlyEnlistmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class EarlyEnlistmentService implements IEarlyEnlistmentService {
    @Autowired
    @Qualifier("userProfileTemplate")
    RestTemplate userRestTemplate;

    @Autowired
    @Qualifier("medicalTemplate")
    RestTemplate medicalTemplate;


    private final AddToQueue addToQueue;
    private final IGuardianConsentRepository iGuardianConsentRepository;
    private final IEarlyEnlistmentRepository iEarlyEnlistmentRepository;
    private final EarlyEnlistmentMapper mapper;
    private final UserInformationService userInformationService;
    private final HeaderToken headerToken;


    public EarlyEnlistmentService(AddToQueue addToQueue, IGuardianConsentRepository iGuardianConsentRepository, IEarlyEnlistmentRepository iEarlyEnlistmentRepository, EarlyEnlistmentMapper mapper,
                                  UserInformationService userInformationService, HeaderToken headerToken) {
        this.addToQueue = addToQueue;
        this.iGuardianConsentRepository = iGuardianConsentRepository;
        this.iEarlyEnlistmentRepository = iEarlyEnlistmentRepository;
        this.mapper = mapper;
        this.userInformationService = userInformationService;
        this.headerToken = headerToken;
    }

    @Override
    public ResponseEntity<?> checkAgeValidation(String authHeader, EarlyEnlistmentDto earlyEnlistmentDto) {
        BigInteger userId = earlyEnlistmentDto.getUserId();
        ApplicationContext context = new AnnotationConfigApplicationContext(ApplicationProperties.class);
        ApplicationProperties properties = context.getBean(ApplicationProperties.class);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", authHeader);
        HttpEntity<String> request = new HttpEntity<>(headers);

        String userUrl = properties.getUserProfileById() + userId;
        ResponseEntity<UserProfileDto> userResponse = userRestTemplate.exchange(userUrl, HttpMethod.GET, request, UserProfileDto.class);
        Date birthDate = Objects.requireNonNull(userResponse.getBody()).getDob();

//        String registrationDateUrl = properties.getActiveRegistrationDate();
//        ResponseEntity<RegistrationDateDto> registrationDateResponse = trainingRestTemplate.exchange(registrationDateUrl, HttpMethod.GET, request, RegistrationDateDto.class);
//        Date registrationDate = Objects.requireNonNull(registrationDateResponse.getBody()).getTrainingDate();
//
//        if (registrationDate == null) {
//            return ResponseEntity.badRequest().body(new MessageResponse("Registration cut-off date not found."));
//        }else {
//
//        }
        Date currentDate = new Date();
        AgeDto ageDto = AgeCalculator.getAge(birthDate, currentDate);
        return ResponseEntity.ok((ageDto));
    }

    @Override
    public ResponseEntity<?> requestGuardianConsent(String authTokenHeader, GuardianConsentRequestDto guardianConsentRequestDto) throws JsonProcessingException {

        BigInteger userId = guardianConsentRequestDto.getUserId();
        String domainName = guardianConsentRequestDto.getDomainName();
        ApplicationContext context = new AnnotationConfigApplicationContext(ApplicationProperties.class);
        ApplicationProperties properties = context.getBean(ApplicationProperties.class);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", authTokenHeader);
        HttpEntity<String> request = new HttpEntity<>(headers);

        String userUrl = properties.getUserProfileById() + userId;
        ResponseEntity<UserProfileDto> userResponse = userRestTemplate.exchange(userUrl, HttpMethod.GET, request, UserProfileDto.class);
        String fullName = Objects.requireNonNull(userResponse.getBody()).getFullName();
        Character gender = Objects.requireNonNull(userResponse.getBody()).getGender();
        String mobileNo = Objects.requireNonNull(userResponse.getBody()).getMobileNo();
        String email = Objects.requireNonNull(userResponse.getBody()).getEmail();
        String guardianName = Objects.requireNonNull(userResponse.getBody()).getGuardianNameFirst();
        String guardianCid = Objects.requireNonNull(userResponse.getBody()).getGuardianCidFirst();
        String guardianMobileNo = Objects.requireNonNull(userResponse.getBody()).getGuardianMobileNoFirst();
        String guardianEmail = Objects.requireNonNull(userResponse.getBody()).getGuardianEmailFirst();
        String relationToGuardian = Objects.requireNonNull(userResponse.getBody()).getRelationToGuardianFirst();
        if (guardianMobileNo == null && guardianEmail == null) {
            return ResponseEntity.badRequest().body(new MessageResponse("Guardian information is incomplete. First, complete guardian details."));
        }

        GuardianConsent guardianConsent = new GuardianConsent();
        GuardianConsent guardianConsentDb = iGuardianConsentRepository.findFirstByOrderByConsentIdDesc();
        BigInteger consentId = guardianConsentDb == null ? BigInteger.ONE : guardianConsentDb.getConsentId().add(BigInteger.ONE);
        guardianConsent.setConsentId(consentId);
        guardianConsent.setUserId(userId);
        guardianConsent.setFullName(fullName);
        guardianConsent.setMobileNo(mobileNo);
        guardianConsent.setEmail(email);
        guardianConsent.setGuardianName(guardianName);
        guardianConsent.setGuardianCid(guardianCid);
        guardianConsent.setGuardianMobileNo(guardianMobileNo);
        guardianConsent.setGuardianEmail(guardianEmail);
        guardianConsent.setRelationToGuardian(relationToGuardian);
        guardianConsent.setConsentRequestDate(new Date());
        guardianConsent.setStatus('P');//P=Pending, A=Approved, R=Denied
        guardianConsent.setCreatedBy(userId);
        guardianConsent.setCreatedDate(new Date());

        // to delete the previous pending status
        GuardianConsent guardianConsentUserId = iGuardianConsentRepository.findByUserIdAndStatus(userId, 'P');
        if (guardianConsentUserId != null) {
            iGuardianConsentRepository.deleteById(guardianConsentUserId.getConsentId());
        }

        iGuardianConsentRepository.save(guardianConsent);

        String consentUrl = domainName + "/guardianConsent?consentId=" + consentId + "" + guardianCid;

        String subject = "Request for Parent or Guardian Consent";
        String messageEmail = "Dear " + guardianName + ",<br></br> We would like to request that " +
                SalutationGenerator.getSalutation(gender) + fullName + " has submitted" +
                " a request for your consent regarding " + SalutationGenerator.getPronoun(gender) + " early enlistment in the National Service Training.<br></br><br></br>" +
                "To provide your consent, please <a href='" + consentUrl + "' target='_blank'>Click here</a><br><br><br><br>" +
                "<small>***This is a system-generated email. Please do not respond to this email.***</small>";

        String messageSms = "Dear " + guardianName + ", We would like to request that " +
                SalutationGenerator.getSalutation(gender) + fullName + " has submitted" +
                " a request for your consent regarding " + SalutationGenerator.getPronoun(gender) + " early enlistment in the National Service Training." +
                " To provide your consent, please click here: " + consentUrl;

        EventBus eventBusEmail = EventBus.withId(guardianEmail, null, null, messageEmail, subject, guardianMobileNo, null, null);
        EventBus eventBusSms = EventBus.withId(null, null, null, messageSms, null, guardianMobileNo, null, null);
        addToQueue.addToQueue("email", eventBusEmail);
        addToQueue.addToQueue("sms", eventBusSms);
        return ResponseEntity.ok(new MessageResponse("Guardian consent requested successfully."));
    }

    @Override
    public ResponseEntity<?> getGuardianConsentStatus(BigInteger userId) {
        GuardianConsent guardianConsent = iGuardianConsentRepository.findFirstByUserIdOrderByConsentRequestDateDesc(userId);
        if (guardianConsent != null) {
            return ResponseEntity.ok(guardianConsent);
        } else {
            return ResponseEntity.badRequest().body(new MessageResponse("Data not found."));
        }
    }

    @Override
    public ResponseEntity<?> applyEarlyEnlistment(String authHeader, EarlyEnlistmentDto earlyEnlistmentDto) throws JsonProcessingException {

        ApplicationContext context = new AnnotationConfigApplicationContext(ApplicationProperties.class);
        ApplicationProperties properties = context.getBean(ApplicationProperties.class);

        BigInteger userId = earlyEnlistmentDto.getUserId();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", authHeader);
        HttpEntity<String> request = new HttpEntity<>(headers);

        String userUrl = properties.getUserProfileById() + userId;
        ResponseEntity<UserProfileDto> userResponse = userRestTemplate.exchange(userUrl, HttpMethod.GET, request, UserProfileDto.class);
        String fullName = Objects.requireNonNull(userResponse.getBody()).getFullName();
        String email = Objects.requireNonNull(userResponse.getBody()).getEmail();
        String mobileNo = Objects.requireNonNull(userResponse.getBody()).getMobileNo();
        Character gender = Objects.requireNonNull(userResponse.getBody()).getGender();

        EarlyEnlistment earlyEnlistmentId = iEarlyEnlistmentRepository.findByUserIdAndStatus(userId, 'P');
        if (earlyEnlistmentId != null) {
            iEarlyEnlistmentRepository.deleteById(earlyEnlistmentId.getEnlistmentId());
        }

        EarlyEnlistment earlyEnlistment = new EarlyEnlistment();

        EarlyEnlistment earlyEnlistmentDb = iEarlyEnlistmentRepository.findFirstByOrderByEnlistmentIdDesc();
        BigInteger enlistmentId = earlyEnlistmentDb == null ? BigInteger.ONE : earlyEnlistmentDb.getEnlistmentId().add(BigInteger.ONE);

        earlyEnlistment.setEnlistmentId(enlistmentId);
        earlyEnlistment.setUserId(userId);

        earlyEnlistment.setApplicationDate(new Date());
        earlyEnlistment.setGender(gender);
        earlyEnlistment.setStatus('P');//P=Pending, A=Approved, R=Rejected
        earlyEnlistment.setCreatedBy(userId);
        earlyEnlistment.setCreatedDate(new Date());

        iEarlyEnlistmentRepository.save(earlyEnlistment);


        String subject = "Early Enlistment";
        String messageEmail = "Dear " + fullName + ",<br></br> You have successfully registered as early enlistment for Training. The Gyalsung " +
                "Head Office will inform you aboutthe medical screeninng and training academy. <br></br><br></br>" +
                "<small>***This is a system-generated email. Please do not respond to this email.***</small>";

        String messageSms = "Dear " + fullName + ", You have successfully registered as early enlistment for Training. The Gyalsung " +
                "Head Office will inform you about the medical screeninng and training academy.";

        EventBus eventBusEmail = EventBus.withId(email, null, null, messageEmail, subject, mobileNo, null, null);
        EventBus eventBusSms = EventBus.withId(null, null, null, messageSms, null, mobileNo, null, null);
        addToQueue.addToQueue("email", eventBusEmail);
        addToQueue.addToQueue("sms", eventBusSms);
        return ResponseEntity.ok(new MessageResponse("Guardian consent requested successfully."));
    }

    @Override
    public ResponseEntity<?> getEarlyEnlistmentStatus(BigInteger userId) {
        EarlyEnlistment earlyEnlistmentDb = iEarlyEnlistmentRepository.findFirstByUserIdOrderByApplicationDateDesc(userId);
        if (earlyEnlistmentDb != null) {
            return ResponseEntity.ok(earlyEnlistmentDb);
        } else {
            return ResponseEntity.badRequest().body(new MessageResponse("Data not found."));
        }
    }

    @Override
    public List<EarlyEnlistmentDto> getEarlyEnlistmentListByCriteria(String authHeader, String enlistmentYear, Character status, Character gender, String cid, Character parentConsentStatus, Integer dzongkhagId) {
        ApplicationContext context = new AnnotationConfigApplicationContext(ApplicationProperties.class);
        ApplicationProperties properties = context.getBean(ApplicationProperties.class);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", authHeader);
        HttpEntity<String> request = new HttpEntity<>(headers);

        enlistmentYear = enlistmentYear.isEmpty() ? null : enlistmentYear;
        cid = cid.isEmpty() ? null : cid;
        List<UserProfileDto> userProfileDtos;
        List<BigInteger> userIdsVal;

        List<EarlyEnlistmentDto> earlyEnlistmentDtoList = iEarlyEnlistmentRepository.getEarlyEnlistmentByEnlistmentYearAndStatusAndGender(enlistmentYear, status, gender)
                .stream()
                .map(mapper::mapToDomain)
                .collect(Collectors.toUnmodifiableList());

        if (!Objects.isNull(cid)) {
            userProfileDtos = userInformationService.getUserInformationByPartialCid(cid, authHeader);
        } else {
            userIdsVal = earlyEnlistmentDtoList
                    .stream()
                    .map(EarlyEnlistmentDto::getUserId)
                    .collect(Collectors.toList());
            userProfileDtos = userInformationService.getUserInformationByListOfIds(userIdsVal, authHeader);

        }

        List<EarlyEnlistmentDto> earlyEnlistmentDtos = new ArrayList<>();


        List<EarlyEnlistmentDto> finalEarlyEnlistmentDtos = earlyEnlistmentDtos;
        earlyEnlistmentDtoList.forEach(item -> {
            UserProfileDto userProfileDto = userProfileDtos.stream()
                    .filter(userProfileDto1 -> item.getUserId().equals(userProfileDto1.getUserId()))
                    .findFirst()
                    .orElse(null);
            GuardianConsent guardianConsent = iGuardianConsentRepository.findFirstByUserIdOrderByConsentRequestDateDesc(item.getUserId());
            String url = properties.getEnlistmentMedBookingById() + item.getEnlistmentId();
            ResponseEntity<EarlyEnlistmentMedBookingDto> medicalBooking = medicalTemplate.exchange(url, HttpMethod.GET, request, EarlyEnlistmentMedBookingDto.class);
            if (!Objects.isNull(userProfileDto)) {
                EarlyEnlistmentDto earlyEnlistmentDto = new EarlyEnlistmentDto();
                earlyEnlistmentDto.setEnlistmentId(item.getEnlistmentId());
                earlyEnlistmentDto.setUserId(item.getUserId());
                earlyEnlistmentDto.setEnlistmentYear(item.getEnlistmentYear());
                earlyEnlistmentDto.setGender(userProfileDto.getGender());
                earlyEnlistmentDto.setGenderName(Gender.fromValue(userProfileDto.getGender()).getName());
                earlyEnlistmentDto.setApplicationDate(item.getApplicationDate());
                earlyEnlistmentDto.setCid(userProfileDto.getCid());
                earlyEnlistmentDto.setFullName(userProfileDto.getFullName());
                earlyEnlistmentDto.setStatus(item.getStatus());
                earlyEnlistmentDto.setDzongkhagId(userProfileDto.getPresentDzongkhagId());
                if (!Objects.isNull(medicalBooking.getBody())) {
                    earlyEnlistmentDto.setHospitalBookingId(Objects.requireNonNull(medicalBooking.getBody()).getHospitalBookingId());
                }
                earlyEnlistmentDto.setEarlyEnlistmentMedBookingDto(medicalBooking.getBody());
                if (!Objects.isNull(guardianConsent)) {
                    earlyEnlistmentDto.setParentConsentStatus(guardianConsent.getStatus());
                    earlyEnlistmentDto.setParentConsentName(Objects.isNull(guardianConsent.getStatus())?"Pending":ParentConsentStatus.fromValue(guardianConsent.getStatus()).getName());

                }
                earlyEnlistmentDto.setStatusName(EnlistmentStatus.fromValue(item.getStatus()).getName());
                finalEarlyEnlistmentDtos.add(earlyEnlistmentDto);
            }

        });
        earlyEnlistmentDtoList = finalEarlyEnlistmentDtos;
        if (!Objects.isNull(parentConsentStatus)) {
            earlyEnlistmentDtoList = finalEarlyEnlistmentDtos.stream()
                    .filter(earlyEnlistmentDto -> !Objects.isNull(earlyEnlistmentDto.getParentConsentStatus()) && earlyEnlistmentDto.getParentConsentStatus().equals(parentConsentStatus))
                    .collect(Collectors.toList());
        }
        if (!Objects.isNull(dzongkhagId)) {
            earlyEnlistmentDtoList = earlyEnlistmentDtoList.stream()
                    .filter(earlyEnlistmentDto -> earlyEnlistmentDto.getDzongkhagId().equals(dzongkhagId))
                    .collect(Collectors.toList());
        }

        return earlyEnlistmentDtoList;
    }

    @Override
    public ResponseEntity<?> approveRejectById(String authHeader, @Valid UpdateEarlyEnlistmentCommand command) {
        iEarlyEnlistmentRepository.findById(command.getEnlistmentId()).ifPresent(d -> {
            d.setStatus(command.getStatus().charAt(0));
            d.setEnlistmentYear(command.getEnlistmentYear());
            d.setRemarks(command.getRemarks());
            iEarlyEnlistmentRepository.save(d);
        });
        EarlyEnlistment earlyEnlistment = iEarlyEnlistmentRepository.findById(command.getEnlistmentId()).get();
        try {
            sendEmailAndSms(authHeader, earlyEnlistment.getUserId(), command.getStatus().charAt(0), earlyEnlistment.getApplicationDate());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        GuardianConsent guardianConsent = iGuardianConsentRepository.findFirstByUserIdOrderByConsentRequestDateDesc(earlyEnlistment.getUserId());
        try {
            sendGuardianEmailAndSms(authHeader, earlyEnlistment.getUserId(), command.getStatus().charAt(0), earlyEnlistment.getApplicationDate(),
                    guardianConsent.getGuardianName(), guardianConsent.getEmail(), guardianConsent.getMobileNo());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok(new MessageResponse(" Saved successfully"));
    }

    private void sendEmailAndSms(String authHeader, BigInteger userId, Character status, Date applicationDate) throws Exception {
        ApplicationContext context = new AnnotationConfigApplicationContext(ApplicationProperties.class);
        ApplicationProperties properties = context.getBean(ApplicationProperties.class);

        HttpEntity<String> httpRequest = headerToken.tokenHeader(authHeader);

        String userUrl = properties.getUserProfileById() + userId;
        ResponseEntity<UserProfileDto> userResponse = userRestTemplate.exchange(userUrl, HttpMethod.GET, httpRequest, UserProfileDto.class);
        String emailMessage = "";
        String subject = "";
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM dd, yyyy");
        String formattedDate = dateFormat.format(applicationDate);

        if (status.equals(ApprovalStatus.APPROVED.value())) {
            subject = "Approved for Early Enlistment";
            emailMessage = "Dear " + Objects.requireNonNull(userResponse.getBody()).getFullName() + ",\n" +
                    "\n" +
                    "In continuation of your early enlistment application submitted on " + formattedDate + ", the Gyalsung Head Office is pleased to inform you that your application for early enlistment has been approved.\n" +
                    " Warm Regards, \n" +
                    "\n" +
                    " Gyalsung HQ \n";

        } else {
            subject = "Rejected for Early Enlistment";
            emailMessage = "Dear " + Objects.requireNonNull(userResponse.getBody()).getFullName() + ",\n" +
                    "\n" +
                    "In continuation of your early enlistment application submitted on " + formattedDate + ", the Gyalsung Head Office is pleased to inform you that your application for early enlistment has been rejected.\n" +
                    " Warm Regards, \n" +
                    "\n" +
                    " Gyalsung HQ \n";
        }

        EventBus eventBus = EventBus.withId(
                Objects.requireNonNull(userResponse.getBody()).getEmail(),
                null,
                null,
                emailMessage,
                subject,
                Objects.requireNonNull(userResponse.getBody()).getMobileNo(),
                null,
                null);

        //todo get data from properties
        addToQueue.addToQueue("email", eventBus);
        addToQueue.addToQueue("sms", eventBus);
    }

    private void sendGuardianEmailAndSms(String authHeader, BigInteger userId, Character status, Date applicationDate,
                                         String guardianName, String email, String mobileNo) throws Exception {
        ApplicationContext context = new AnnotationConfigApplicationContext(ApplicationProperties.class);
        ApplicationProperties properties = context.getBean(ApplicationProperties.class);

        HttpEntity<String> httpRequest = headerToken.tokenHeader(authHeader);

        String userUrl = properties.getUserProfileById() + userId;
        ResponseEntity<UserProfileDto> userResponse = userRestTemplate.exchange(userUrl, HttpMethod.GET, httpRequest, UserProfileDto.class);
        String emailMessage = "";
        String subject = "";
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM dd, yyyy");
        String formattedDate = dateFormat.format(applicationDate);

        if (status.equals(ApprovalStatus.APPROVED.value())) {
            subject = "Approved for Early Enlistment";
            emailMessage = "Dear " + guardianName + ",\n" +
                    "\n" +
                    "In continuation of your early enlistment application submitted on " + formattedDate + ",by your children," + Objects.requireNonNull(userResponse.getBody()).getFullName() + ",the Gyalsung Head Office is pleased to inform you that your application for early enlistment has been approved.\n" +
                    " Warm Regards, \n" +
                    "\n" +
                    " Gyalsung HQ \n";

        } else {
            subject = "Rejected for Early Enlistment";
            emailMessage = "Dear " + guardianName + ",\n" +
                    "\n" +
                    "In continuation of your early enlistment application submitted on " + formattedDate + ",by your children," + Objects.requireNonNull(userResponse.getBody()).getFullName() + ",the Gyalsung Head Office is pleased to inform you that your application for early enlistment has been rejected.\n" +
                    " Warm Regards, \n" +
                    "\n" +
                    " Gyalsung HQ \n";
        }

        EventBus eventBus = EventBus.withId(
                email,
                null,
                null,
                emailMessage,
                subject,
                mobileNo,
                null,
                null);

        //todo get data from properties
        addToQueue.addToQueue("email", eventBus);
        addToQueue.addToQueue("sms", eventBus);
    }
}
