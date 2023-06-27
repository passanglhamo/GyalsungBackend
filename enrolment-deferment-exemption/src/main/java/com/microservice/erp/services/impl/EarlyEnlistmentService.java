package com.microservice.erp.services.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.microservice.erp.domain.dto.*;
import com.microservice.erp.domain.entities.EarlyEnlistment;
import com.microservice.erp.domain.entities.GuardianConsent;
import com.microservice.erp.domain.helper.AgeCalculator;
import com.microservice.erp.domain.helper.AgeDto;
import com.microservice.erp.domain.helper.MessageResponse;
import com.microservice.erp.domain.helper.SalutationGenerator;
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
import java.util.*;
import java.util.stream.Collectors;

@Service
public class EarlyEnlistmentService implements IEarlyEnlistmentService {
    @Autowired
    @Qualifier("userProfileTemplate")
    RestTemplate userRestTemplate;

//    @Autowired
//    @Qualifier("trainingManagementTemplate")
//    RestTemplate trainingRestTemplate;

    @Autowired
    @Qualifier("medicalTemplate")
    RestTemplate medicalTemplate;

    private final AddToQueue addToQueue;
    private final IGuardianConsentRepository iGuardianConsentRepository;
    private final IEarlyEnlistmentRepository iEarlyEnlistmentRepository;
    private final EarlyEnlistmentMapper mapper;
    private final UserInformationService userInformationService;


    public EarlyEnlistmentService(AddToQueue addToQueue, IGuardianConsentRepository iGuardianConsentRepository, IEarlyEnlistmentRepository iEarlyEnlistmentRepository, EarlyEnlistmentMapper mapper,
                                  UserInformationService userInformationService) {
        this.addToQueue = addToQueue;
        this.iGuardianConsentRepository = iGuardianConsentRepository;
        this.iEarlyEnlistmentRepository = iEarlyEnlistmentRepository;
        this.mapper = mapper;
        this.userInformationService = userInformationService;
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


    private static int getLastDayOfMonth(int month, int year) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, 1);
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
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
        String fullName = Objects.requireNonNull(userResponse.getBody()).getFullName().trim();
        Character gender = Objects.requireNonNull(userResponse.getBody()).getGender();
        String guardianName = Objects.requireNonNull(userResponse.getBody()).getGuardianNameFirst().trim();
        String guardianCid = Objects.requireNonNull(userResponse.getBody()).getGuardianCidFirst().trim();
        String guardianMobileNo = Objects.requireNonNull(userResponse.getBody()).getGuardianMobileNoFirst().trim();
        String guardianEmail = Objects.requireNonNull(userResponse.getBody()).getGuardianEmailFirst().trim();
        String relationToGuardian = Objects.requireNonNull(userResponse.getBody()).getRelationToGuardianFirst().trim();

        GuardianConsent guardianConsent = new GuardianConsent();

        GuardianConsent guardianConsentDb = iGuardianConsentRepository.findFirstByOrderByConsentIdDesc();
        BigInteger consentId = guardianConsentDb == null ? BigInteger.ONE : guardianConsentDb.getConsentId().add(BigInteger.ONE);

        guardianConsent.setConsentId(consentId);
        guardianConsent.setUserId(userId);
        guardianConsent.setGuardianName(guardianName);
        guardianConsent.setGuardianCid(guardianCid);
        guardianConsent.setGuardianMobileNo(guardianMobileNo);
        guardianConsent.setGuardianEmail(guardianEmail);
        guardianConsent.setRelationToGuardian(relationToGuardian);
        guardianConsent.setConsentRequestDate(new Date());
        guardianConsent.setStatus('P');//P=Pending, A=Approved, R=Rejected
        guardianConsent.setCreatedBy(userId);
        guardianConsent.setCreatedDate(new Date());
        iGuardianConsentRepository.save(guardianConsent);

        String consentUrl = domainName + "/guardianConsent?consentId=" + consentId + "&randomId=" + guardianCid;

//        String consentUrl = domainName + "/guardianConsent?consentId=" + consentId+;

        String subject = "Request for Parent or Guardian Consent";
        String messageEmail = "Dear " + guardianName + ",<br></br> We would like to request that " +
                SalutationGenerator.getSalutation(gender) + fullName + " has submitted" +
                " a request for your consent regarding " + SalutationGenerator.getPronoun(gender) + " early enlistment in the National Service Training.<br></br><br></br>" +
                "To provide your consent, please <a href='" + consentUrl + "' target='_blank'>Click here</a><br><br><br><br>" +
                "<small>***This is a system-generated email. Please do not respond to this email.***</small>";

        String messageSms = "Dear " + guardianName + ", We would like to request that " +
                SalutationGenerator.getSalutation(gender) + fullName + " has submitted" +
                " a request for your consent regarding " + SalutationGenerator.getPronoun(gender) + " early enlistment in the National Service Training." +
                " To provide your consent, please click here:" + consentUrl;

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
        String fullName = Objects.requireNonNull(userResponse.getBody()).getFullName().trim();
        String email = Objects.requireNonNull(userResponse.getBody()).getEmail().trim();
        String mobileNo = Objects.requireNonNull(userResponse.getBody()).getMobileNo().trim();
        Character gender = Objects.requireNonNull(userResponse.getBody()).getGender();


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

        List<EarlyEnlistmentDto> earlyEnlistmentDtoList = iEarlyEnlistmentRepository.getEarlyEnlistmentByEnlistmentYearAndStatus(enlistmentYear, status)
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
                    .filter(userProfileDto1 -> item.getUserId().equals(userProfileDto1.getId()))
                    .findFirst()
                    .orElse(null);
            GuardianConsent guardianConsent = iGuardianConsentRepository.findFirstByUserIdOrderByConsentRequestDateDesc(item.getUserId());
            String url = properties.getEnlistmentMedBookingByUserIdAndId() + item.getUserId() + "&earlyEnlistmentId=" + item.getEnlistmentId();
            ResponseEntity<EarlyEnlistmentMedBookingDto> medicalBooking = medicalTemplate.exchange(url, HttpMethod.GET, request, EarlyEnlistmentMedBookingDto.class);
            if (!Objects.isNull(userProfileDto)) {
                EarlyEnlistmentDto earlyEnlistmentDto = new EarlyEnlistmentDto();
                earlyEnlistmentDto.setEnlistmentId(item.getEnlistmentId());
                earlyEnlistmentDto.setUserId(item.getUserId());
                earlyEnlistmentDto.setEnlistmentYear(item.getEnlistmentYear());
                //earlyEnlistmentDto.setGender(item.getGender());
                earlyEnlistmentDto.setApplicationDate(item.getApplicationDate());
                earlyEnlistmentDto.setCid(userProfileDto.getCid());
                earlyEnlistmentDto.setFullName(userProfileDto.getFullName());
                earlyEnlistmentDto.setStatus(item.getStatus());
                earlyEnlistmentDto.setDzongkhagId(userProfileDto.getPresentDzongkhagId());
                earlyEnlistmentDto.setEarlyEnlistmentMedBookingDto(medicalBooking.getBody());
                if (!Objects.isNull(guardianConsent)) {
                    earlyEnlistmentDto.setParentConsentStatus(guardianConsent.getStatus());
                }
                finalEarlyEnlistmentDtos.add(earlyEnlistmentDto);
            }

        });
        earlyEnlistmentDtoList = finalEarlyEnlistmentDtos;
        if(!Objects.isNull(parentConsentStatus)){
            earlyEnlistmentDtoList =  finalEarlyEnlistmentDtos.stream()
                    .filter(earlyEnlistmentDto -> earlyEnlistmentDto.getParentConsentStatus().equals(parentConsentStatus))
                    .collect(Collectors.toList());
        }
        if(!Objects.isNull(dzongkhagId)){
            earlyEnlistmentDtoList =  earlyEnlistmentDtoList.stream()
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

        return ResponseEntity.ok(new MessageResponse(" Saved successfully"));
    }
}
