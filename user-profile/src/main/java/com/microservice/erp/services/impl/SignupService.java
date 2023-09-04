package com.microservice.erp.services.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.microservice.erp.domain.dto.*;
import com.microservice.erp.domain.entities.*;
import com.microservice.erp.domain.helper.OTPGenerator;
import com.microservice.erp.domain.repositories.IAgeCriteriaRepository;
import com.microservice.erp.domain.repositories.ISignupEmailVerificationCodeRepository;
import com.microservice.erp.domain.repositories.ISignupSmsOtpRepository;
import com.microservice.erp.domain.repositories.IUserInfoRepository;
import com.microservice.erp.services.iServices.ISignupService;
import com.squareup.okhttp.OkHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;
import org.modelmapper.ModelMapper;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
//import org.wso2.client.api.ApiClient;
//import org.wso2.client.api.ApiException;
//import org.wso2.client.api.DCRC_CitizenDetailsAPI.DefaultApi;
//import org.wso2.client.model.DCRC_CitizenDetailsAPI.CitizenDetailsResponse;
//import org.wso2.client.model.DCRC_CitizenDetailsAPI.CitizendetailsObj;
//import org.wso2.client.model.DCRC_CitizenDetailsAPI.ParentdetailObj;
//import org.wso2.client.model.DCRC_CitizenDetailsAPI.ParentdetailResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class SignupService implements ISignupService {
    private final CitizenDetailApiService citizenDetailApiService;
    private final ISignupSmsOtpRepository iSignupSmsOtpRepository;
    private final ISignupEmailVerificationCodeRepository iSignupEmailVerificationCodeRepository;
    private final IUserInfoRepository iUserInfoRepository;
    private final IAgeCriteriaRepository iAgeCriteriaRepository;
    private final AddToQueue addToQueue;

    public SignupService(CitizenDetailApiService citizenDetailApiService, ISignupSmsOtpRepository iSignupSmsOtpRepository
            , ISignupEmailVerificationCodeRepository iSignupEmailVerificationCodeRepository, IUserInfoRepository iUserInfoRepository
            , IAgeCriteriaRepository iAgeCriteriaRepository, AddToQueue addToQueue) {
        this.citizenDetailApiService = citizenDetailApiService;
        this.iSignupSmsOtpRepository = iSignupSmsOtpRepository;
        this.iSignupEmailVerificationCodeRepository = iSignupEmailVerificationCodeRepository;
        this.iUserInfoRepository = iUserInfoRepository;
        this.iAgeCriteriaRepository = iAgeCriteriaRepository;
        this.addToQueue = addToQueue;
    }


    @Override
    public ResponseEntity<?> getCitizenDetails(String cid, String dob) throws ParseException, IOException {
        // to check if user is already exist or not by cid
        Optional<UserInfo> userInfo = iUserInfoRepository.findByCid(cid);
        if (userInfo.isPresent()) {
            return ResponseEntity.badRequest().body(new MessageResponse("User with CID " + cid + " already exists in the system."));
        }
        //to check age eligible age first
        AgeCriteria ageCriteria = iAgeCriteriaRepository.findTopByOrderByMinimumAgeDesc();
        if (ageCriteria == null) {
            return ResponseEntity.badRequest().body(new MessageResponse("Something went wrong. Please try again after some time."));
        }
        Integer minimumAge = ageCriteria.getMinimumAge();
        Integer maximumAge = ageCriteria.getMaximumAge();

        Date birthDate = new SimpleDateFormat("dd/MM/yyyy").parse(dob);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(birthDate);
        Calendar now = Calendar.getInstance();
        int age = now.get(Calendar.YEAR) - calendar.get(Calendar.YEAR);
        if (age < minimumAge || age > maximumAge) {
            return ResponseEntity.badRequest().body(new MessageResponse("You do not meet the age criteria."));
        }

        return validateCitizenDetails(cid, dob);
    }

    @Override
    public ResponseEntity<?> receiveOtp(NotificationRequestDto notificationRequestDto) throws JsonProcessingException {
//        Optional<UserInfo> userInfoDB = iUserInfoRepository.findByMobileNo(notificationRequestDto.getMobileNo());
//        if (userInfoDB.isPresent()) {
//            return ResponseEntity.badRequest().body(new MessageResponse("The mobile number you have entered is already in use. Please try a different one."));
//        }
        String otp = OTPGenerator.generateOtp();
        String message = "Your OTP for Gyalsung Registration is " + otp + " Please use this within 3 minutes.";
        SignupSmsOtp signupSmsOtp = new SignupSmsOtp();
        signupSmsOtp.setMobileNo(notificationRequestDto.getMobileNo());
        signupSmsOtp.setOtp(otp);
        signupSmsOtp.setDate(new Date());
        signupSmsOtp.setExpiryTime(180);//seconds
        iSignupSmsOtpRepository.save(signupSmsOtp);

        EventBus eventBusSms = EventBus.withId(null, null, null, message, null, notificationRequestDto.getMobileNo());

        addToQueue.addToQueue("sms", eventBusSms);
        return ResponseEntity.ok("Received otp");
    }

    @Override
    public ResponseEntity<?> verifyOtp(NotificationRequestDto notificationRequestDto) {
        SignupSmsOtp signupSmsOtp = iSignupSmsOtpRepository.findByMobileNo(notificationRequestDto.getMobileNo());
        if (signupSmsOtp == null) {
            return ResponseEntity.badRequest().body(new MessageResponse("OTP not found matching mobile number " + notificationRequestDto.getMobileNo()));
        }
        if (Objects.equals(signupSmsOtp.getOtp(), notificationRequestDto.getOtp())) {
            return ResponseEntity.ok("");
        } else {
            return ResponseEntity.badRequest().body(new MessageResponse("The OTP didn't match."));
        }
    }

    @Override
    public ResponseEntity<?> receiveEmailVcode(NotificationRequestDto notificationRequestDto) throws Exception {
//        Optional<UserInfo> userInfoDB = iUserInfoRepository.findByEmail(notificationRequestDto.getEmail());
//        if (userInfoDB.isPresent()) {
//            return ResponseEntity.badRequest().body(new MessageResponse("The email address you have entered is already in use. Please try different one."));
//        }
        String verificationCode = OTPGenerator.generateOtp();

        String subject = "Email Verification";
        String message = "Dear User,<br></br>" +
                " The verification code for Gyalsung System is " + verificationCode + ".<br></br><br></br>" +
                "<small>***This is a system-generated email. Please do not respond to this email.***</small>";

        EventBus eventBusEmail = EventBus.withId(notificationRequestDto.getEmail(), null, null, message, subject, null);
        addToQueue.addToQueue("email", eventBusEmail);

        SignupEmailVerificationCode signupEmailVerificationCode = new SignupEmailVerificationCode();
        signupEmailVerificationCode.setEmail(notificationRequestDto.getEmail());
        signupEmailVerificationCode.setVerificationCode(verificationCode);
        signupEmailVerificationCode.setDate(new Date());
        signupEmailVerificationCode.setExpiryTime(180);//seconds
        iSignupEmailVerificationCodeRepository.save(signupEmailVerificationCode);
        return ResponseEntity.ok("Receive email");
    }


    @Override
    public ResponseEntity<?> verifyEmailVcode(NotificationRequestDto notificationRequestDto) {
        SignupEmailVerificationCode signupEmailVerificationCode = iSignupEmailVerificationCodeRepository.findByEmail(notificationRequestDto.getEmail());

        if (signupEmailVerificationCode == null) {
            return ResponseEntity.badRequest().body(new MessageResponse("Verification code not found matching associated with email " + notificationRequestDto.getEmail()));
        }

        if (Objects.equals(signupEmailVerificationCode.getVerificationCode(), notificationRequestDto.getVerificationCode())) {
            return ResponseEntity.ok("");
        } else {
            return ResponseEntity.badRequest().body(new MessageResponse("The email verification code didn't match."));
        }
    }

    @Override
    public ResponseEntity<?> signup(SignupRequestDto signupRequestDto) throws ParseException, IOException {

        //check already registered not by CID
        Optional<UserInfo> userInfoDB = iUserInfoRepository.findByCid(signupRequestDto.getCid());
        if (userInfoDB.isPresent()) {
            return ResponseEntity.badRequest().body(new MessageResponse("User with CID " + signupRequestDto.getCid() + " already exist."));
        }

//  to save guardian info, both the parents need will be guardian by default. If Parents are expired, then the separate guardian need to add from profile
        UserInfo userInfo = new ModelMapper().map(signupRequestDto, UserInfo.class);
        ResponseEntity<CitizenDetailDto> validateCitizenDetails = (ResponseEntity<CitizenDetailDto>) validateCitizenDetails(signupRequestDto.getCid(), signupRequestDto.getBirthDate());
        Date birthDate = new SimpleDateFormat("dd/MM/yyyy").parse(validateCitizenDetails.getBody().getDob());
        String birthDateString = validateCitizenDetails.getBody().getDob();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(birthDate);
        Calendar now = Calendar.getInstance();
        int age = now.get(Calendar.YEAR) - calendar.get(Calendar.YEAR);

        AgeCriteria ageCriteria = iAgeCriteriaRepository.findTopByOrderByMinimumAgeDesc();
        if (ageCriteria == null) {
            return ResponseEntity.badRequest().body(new MessageResponse("Something went wrong. Please try again after some time."));
        }
        Integer minimumAge = ageCriteria.getMinimumAge();
        Integer maximumAge = ageCriteria.getMaximumAge();

        if (age < minimumAge || age > maximumAge) {
            return ResponseEntity.badRequest().body(new MessageResponse("You do not meet the age criteria."));
        }
        userInfo.setSignupUser('Y');
        userInfo.setDob(birthDate);
        userInfo.setUsername(validateCitizenDetails.getBody().getCid());

        userInfo.setCid(validateCitizenDetails.getBody().getCid());
        userInfo.setFullName(validateCitizenDetails.getBody().getFullName());
        userInfo.setGender(validateCitizenDetails.getBody().getGender());
        userInfo.setFatherName(validateCitizenDetails.getBody().getFatherName());
        userInfo.setMotherName(validateCitizenDetails.getBody().getMotherName());
        userInfo.setMotherCid(validateCitizenDetails.getBody().getMotherCid());
        userInfo.setPermanentPlaceName(validateCitizenDetails.getBody().getVillageName());
        userInfo.setPermanentGeog(validateCitizenDetails.getBody().getGeogName());
        userInfo.setPermanentDzongkhag(validateCitizenDetails.getBody().getDzongkhagName());

        String fatherName = validateCitizenDetails.getBody().getFatherName();
        String motherName = validateCitizenDetails.getBody().getMotherName();

        boolean fatherExpired = false;
        if (fatherName.toUpperCase().contains("LATE") || fatherName.toUpperCase().contains("LT.")) {
            fatherExpired = true;
        }

        boolean motherExpired = false;
        if (motherName.toUpperCase().contains("LATE") || motherName.toUpperCase().contains("LT.")) {
            motherExpired = true;
        }

        if (fatherExpired) {
            if (!motherExpired) {
                userInfo.setGuardianNameFirst(validateCitizenDetails.getBody().getMotherName());
                userInfo.setGuardianCidFirst(validateCitizenDetails.getBody().getMotherCid());
                userInfo.setRelationToGuardianFirst("Mother");
            }
        } else {
            userInfo.setGuardianNameFirst(validateCitizenDetails.getBody().getFatherName());
            userInfo.setGuardianCidFirst(validateCitizenDetails.getBody().getFatherCid());
            userInfo.setRelationToGuardianFirst("Father");
            if (!motherExpired) {
                userInfo.setGuardianNameSecond(validateCitizenDetails.getBody().getMotherName());
                userInfo.setGuardianCidSecond(validateCitizenDetails.getBody().getMotherCid());
                userInfo.setRelationToGuardianSecond("Mother");
            }
        }

        //Mobile number verification OTP received from dto must be equal to backend
        String phoneOrEmailVerification = signupRequestDto.getPhoneOrEmailVerification();
        NotificationRequestDto notificationRequestDto = new NotificationRequestDto();

        if (phoneOrEmailVerification.equals("phone")) {
            userInfo.setEmail(null);
            notificationRequestDto.setMobileNo(signupRequestDto.getMobileNo());
            notificationRequestDto.setOtp(signupRequestDto.getOtp());
            ResponseEntity<?> responseEntity = verifyOtp(notificationRequestDto);
            if (responseEntity.getStatusCode().value() != HttpStatus.OK.value()) {
                return ResponseEntity.badRequest().body(new MessageResponse("The OTP didn't match."));
            } else {
                iSignupSmsOtpRepository.deleteById(notificationRequestDto.getMobileNo());//delete OTP after validation
            }
        } else {
            //Email verification code received from dto must be equal to backend
            userInfo.setMobileNo(null);
            notificationRequestDto.setEmail(signupRequestDto.getEmail());
            notificationRequestDto.setVerificationCode(signupRequestDto.getVerificationCode());
            ResponseEntity<?> responseEntityEmail = verifyEmailVcode(notificationRequestDto);
            if (responseEntityEmail.getStatusCode().value() != HttpStatus.OK.value()) {
                return ResponseEntity.badRequest().body(new MessageResponse("The email verification code didn't match."));
            } else {
                iSignupEmailVerificationCodeRepository.deleteById(signupRequestDto.getEmail());
            }
        }

        //Password must be equal to confirm password
        if (!Objects.equals(signupRequestDto.getPassword(), signupRequestDto.getConfirmPassword())) {
            return ResponseEntity.badRequest().body(new MessageResponse("The password didn't match."));
        }
        userInfo.setPresentCountry(signupRequestDto.getPresentCountry());
//        UserInfo userInfoDb = iUserInfoRepository.findFirstByOrderByUserIdDesc();
//        BigInteger userId = userInfoDb == null ? BigInteger.ONE : userInfoDb.getUserId().add(BigInteger.ONE);
//          userInfo.setUserId(userId);
        userInfo.setCreatedDate(new Date());
//        userInfo.setCreatedBy(userId);
        UserInfo userInfoSave = iUserInfoRepository.save(userInfo);
        List<BigInteger> roles = new ArrayList<>();
//         queue following data: password, roles, email, username, userId in auth microservices
        EventBusUser eventBusSms = EventBusUser.withId(userInfoSave.getUserId(), 'A', userInfo.getCid(), birthDateString, userInfo.getEmail(), userInfo.getMobileNo()
                , userInfo.getUsername(), signupRequestDto.getPassword(), userInfo.getSignupUser(), roles);
        addToQueue.addToUserQueue("addUser", eventBusSms);
        return ResponseEntity.ok(new MessageResponse("Registered successfully."));
    }

    @Override
    public ResponseEntity<?> getEligiblePopulationByYearAndAge(String dateString) throws IOException, ParseException, UnirestException {
        String endPointUrl;
        try {
            Properties props = PropertiesLoaderUtils.loadProperties(new ClassPathResource("/apiConfig/dcrcApi.properties"));
            endPointUrl = props.getProperty("getEligiblePopulationByYearAndAge.endPointURL");
        } catch (IOException ex) {
            throw new RuntimeException("Error loading properties file", ex);
        }
//todo:need to pass age dynamic
        String userUrl = String.format("%s/%s/%s", endPointUrl, dateString, "18");
        URL url = new URL(userUrl);

        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        ApiAccessToken apiAccessToken = citizenDetailApiService.getApplicationToken();
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("Authorization", "Bearer " + apiAccessToken.getAccess_token());

        int responseCode = con.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
                JSONArray details = new JSONObject(in.readLine()).getJSONObject("details").getJSONArray("detail");

                Set<String> cidNos = IntStream.range(0, details.length())
                        .mapToObj(i -> details.getJSONObject(i).getString("cidNo"))
                        .collect(Collectors.toSet());

                List<UserInfo> userInfoList = iUserInfoRepository.findByCidIn(cidNos);

                Map<String, Boolean> existsByCidNoMap = userInfoList.stream()
                        .collect(Collectors.toMap(UserInfo::getCid, userInfo -> true));

                List<ExpectedPopulationDto> expectedList = IntStream.range(0, details.length())
                        .mapToObj(i -> {
                            JSONObject item = details.getJSONObject(i);
                            String cidNo = item.getString("cidNo");
                            ExpectedPopulationDto expectedPopulationDto = new ExpectedPopulationDto();
                            expectedPopulationDto.setCidNo(cidNo);
                            expectedPopulationDto.setDob(item.getString("dob"));
                            expectedPopulationDto.setName(item.getString("name"));
                            expectedPopulationDto.setGender(item.getString("gender"));
                            expectedPopulationDto.setIsRegistered(existsByCidNoMap.get(cidNo));
                            return expectedPopulationDto;
                        })
                        .collect(Collectors.toList());
                return ResponseEntity.ok().body(expectedList);
            }
        } else {
            return ResponseEntity.badRequest().body(new MessageResponse("Data not found."));
        }
    }

    @Override
    public ResponseEntity<?> getListOfStudentsByClassAndYear(String className, String year) throws IOException, ParseException, UnirestException {
        String endPointUrl;
        try {
            Properties props = PropertiesLoaderUtils.loadProperties(new ClassPathResource("/apiConfig/dcrcApi.properties"));
            endPointUrl = props.getProperty("getEligiblePopulationClassAndYear.endPointURL");
        } catch (IOException ex) {
            throw new RuntimeException("Error loading properties file", ex);
        }
        String userUrl = String.format("%s/%s/%s", endPointUrl, className, year);
        URL url = new URL(userUrl);

        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        ApiAccessToken apiAccessToken = citizenDetailApiService.getApplicationToken();
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("Authorization", "Bearer " + apiAccessToken.getAccess_token());

        int responseCode = con.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
                JSONArray details = new JSONObject(in.readLine()).getJSONObject("StudentDetails").getJSONArray("StudentDetail");

                Set<String> cidNos = IntStream.range(0, details.length())
                        .mapToObj(i -> details.getJSONObject(i).get("CidNo").toString())
                        .collect(Collectors.toSet());

                List<UserInfo> userInfoList = iUserInfoRepository.findByCidIn(cidNos);

                Map<String, Boolean> existsByCidNoMap = userInfoList.stream()
                        .collect(Collectors.toMap(UserInfo::getCid, userInfo -> true));

                List<MoeExpectedPopulationDto> moeExpectedPopulationDtos = IntStream.range(0, details.length())
                        .mapToObj(i -> {
                            JSONObject item = details.getJSONObject(i);
                            String cidNo = item.get("CidNo").toString();
                            MoeExpectedPopulationDto moeExpectedPopulationDto = new MoeExpectedPopulationDto();
                            moeExpectedPopulationDto.setCidNo(cidNo);
                            moeExpectedPopulationDto.setDob(item.get("DateOfBirth").toString());
                            moeExpectedPopulationDto.setName(item.get("student_name").toString());
                            moeExpectedPopulationDto.setGender(item.get("gender").toString());
                            moeExpectedPopulationDto.setSchoolName(item.get("school_name").toString());
                            moeExpectedPopulationDto.setStream(item.get("stream").toString());
                            moeExpectedPopulationDto.setClassName(item.get("class").toString());
                            moeExpectedPopulationDto.setSection(item.get("section").toString());
                            moeExpectedPopulationDto.setIsRegistered(existsByCidNoMap.get(cidNo));
                            return moeExpectedPopulationDto;
                        })
                        .collect(Collectors.toList());
                return ResponseEntity.ok().body(moeExpectedPopulationDtos);
            }
        } else {
            return ResponseEntity.badRequest().body(new MessageResponse("Data not found."));
        }

    }

    private static List<JSONObject> toList(JSONArray array) {
        return IntStream.range(0, array.length())
                .mapToObj(array::getJSONObject)
                .collect(Collectors.toList());
    }


    @Override
    public ResponseEntity<?> getPersonDetailsByCid(String cid) throws IOException, ParseException {
        CitizenDetailDto citizenDetailDto = new CitizenDetailDto();
        Resource resource = new ClassPathResource("/apiConfig/dcrcApi.properties");
        Properties props = PropertiesLoaderUtils.loadProperties(resource);
        String getCitizenDetails = props.getProperty("getCitizenDetails.endPointURL");
        OkHttpClient httpClient = new OkHttpClient();
        httpClient.setConnectTimeout(10000, TimeUnit.MILLISECONDS);
        httpClient.setReadTimeout(10000, TimeUnit.MILLISECONDS);
//        ApiClient apiClient = new ApiClient();
//        apiClient.setHttpClient(httpClient);
//        apiClient.setBasePath(getCitizenDetails);
//        ApiAccessToken apiAccessToken = citizenDetailApiService.getApplicationToken();
//        apiClient.setAccessToken(apiAccessToken.getAccess_token());
//
//        DefaultApi api = new DefaultApi(apiClient);
//        CitizenDetailsResponse citizenDetailsResponse = api.citizendetailsCidGet(cid);
//        ParentdetailResponse parentdetailResponse = api.parentdetailsCidGet(cid);
//        if (citizenDetailsResponse.getCitizenDetailsResponse().getCitizenDetail() != null && !citizenDetailsResponse.getCitizenDetailsResponse().getCitizenDetail().isEmpty()) {
//            CitizendetailsObj citizendetailsObj = citizenDetailsResponse.getCitizenDetailsResponse().getCitizenDetail().get(0);
//            ParentdetailObj parentdetailObj = parentdetailResponse.getParentDetailResponse().getParentDetail().get(0);
//            char genderChar = citizendetailsObj.getGender().charAt(0);
//            String genderName = "Male";
//            if (genderChar == 'F') {
//                genderName = "Female";
//            } else if (genderChar == 'O') {
//                genderName = "Other";
//            }
//            citizenDetailDto.setFullName(citizendetailsObj.getFirstName() + " " + citizendetailsObj.getMiddleName() + " " + citizendetailsObj.getLastName());
//            citizenDetailDto.setFullName(citizenDetailDto.getFullName().replaceAll("null", ""));
//            citizenDetailDto.setCid(citizendetailsObj.getCid());
//            citizenDetailDto.setDob(citizendetailsObj.getDob());
//            citizenDetailDto.setGender(genderChar);
//            citizenDetailDto.setGenderName(genderName);
//            citizenDetailDto.setFatherName(citizendetailsObj.getFatherName());
//            citizenDetailDto.setFatherCid(parentdetailObj.getFatherCID());
//            citizenDetailDto.setMotherName(citizendetailsObj.getMotherName());
//            citizenDetailDto.setMotherCid(parentdetailObj.getMotherCID());
//            citizenDetailDto.setVillageName(citizendetailsObj.getVillageName());
//            citizenDetailDto.setGeogName(citizendetailsObj.getGewogName());
//            citizenDetailDto.setDzongkhagName(citizendetailsObj.getDzongkhagName());
//            citizenDetailDto.setHouseNo(citizendetailsObj.getHouseNo());
//            citizenDetailDto.setThramNo(citizendetailsObj.getThramNo());
//
//        } else {
//            return ResponseEntity.badRequest().body(new MessageResponse("No information found matching CID No " + cid));
//        }
        return ResponseEntity.ok(citizenDetailDto);
    }

    @Override
    public ResponseEntity<?> getSignUpUsers(String tillDate) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = format.parse(tillDate);
        List<UserInfo> saUsers = iUserInfoRepository.getAllUserTillDate('Y', date);
        return ResponseEntity.ok(saUsers);
    }

    public ResponseEntity<?> validateCitizenDetails(String cid, String dob) throws ParseException, IOException {
        CitizenDetailDto citizenDetailDto = new CitizenDetailDto();
        Resource resource = new ClassPathResource("/apiConfig/dcrcApi.properties");
        Properties props = PropertiesLoaderUtils.loadProperties(resource);
        String getCitizenDetails = props.getProperty("getCitizenDetails.endPointURL");
        OkHttpClient httpClient = new OkHttpClient();
        httpClient.setConnectTimeout(10000, TimeUnit.MILLISECONDS);
        httpClient.setReadTimeout(10000, TimeUnit.MILLISECONDS);
//        ApiClient apiClient = new ApiClient();
//        apiClient.setHttpClient(httpClient);
//        apiClient.setBasePath(getCitizenDetails);
//        ApiAccessToken apiAccessToken = citizenDetailApiService.getApplicationToken();
//        apiClient.setAccessToken(apiAccessToken.getAccess_token());
//
//        DefaultApi api = new DefaultApi(apiClient);
//        CitizenDetailsResponse citizenDetailsResponse = api.citizendetailsCidGet(cid);
//        ParentdetailResponse parentdetailResponse = api.parentdetailsCidGet(cid);
//        if (citizenDetailsResponse.getCitizenDetailsResponse().getCitizenDetail() != null && !citizenDetailsResponse.getCitizenDetailsResponse().getCitizenDetail().isEmpty()) {
//            CitizendetailsObj citizendetailsObj = citizenDetailsResponse.getCitizenDetailsResponse().getCitizenDetail().get(0);
//            ParentdetailObj parentdetailObj = parentdetailResponse.getParentDetailResponse().getParentDetail().get(0);
//            char genderChar = citizendetailsObj.getGender().charAt(0);
//            String genderName = "Male";
//            if (genderChar == 'F') {
//                genderName = "Female";
//            } else if (genderChar == 'O') {
//                genderName = "Other";
//            }
//            String censusDob = citizendetailsObj.getDob();
//            if (!censusDob.equals(dob)) {
//                return ResponseEntity.badRequest().body(new MessageResponse("CID or date of birth is incorrect."));
//            }
//            citizenDetailDto.setFullName(citizendetailsObj.getFirstName() + " " + citizendetailsObj.getMiddleName() + " " + citizendetailsObj.getLastName());
//            citizenDetailDto.setFullName(citizenDetailDto.getFullName().replaceAll("null", ""));
//            citizenDetailDto.setCid(citizendetailsObj.getCid());
//            citizenDetailDto.setDob(censusDob);
//            citizenDetailDto.setGender(genderChar);
//            citizenDetailDto.setGenderName(genderName);
//            citizenDetailDto.setFatherName(citizendetailsObj.getFatherName());
//            citizenDetailDto.setFatherCid(parentdetailObj.getFatherCID());
//            citizenDetailDto.setMotherName(citizendetailsObj.getMotherName());
//            citizenDetailDto.setMotherCid(parentdetailObj.getMotherCID());
//            citizenDetailDto.setVillageName(citizendetailsObj.getVillageName());
//            citizenDetailDto.setGeogName(citizendetailsObj.getGewogName());
//            citizenDetailDto.setDzongkhagName(citizendetailsObj.getDzongkhagName());
//            citizenDetailDto.setHouseNo(citizendetailsObj.getHouseNo());
//            citizenDetailDto.setThramNo(citizendetailsObj.getThramNo());
//
//            //set father name and father cid as guardian 1 name and guardian 1 cid, and mother name and cid as guardian 2 name and guardian 2 cid respectively
////            citizenDetailDto.setGuardianNameFirst(citizendetailsObj.getFatherName());
////            citizenDetailDto.setGuardianCidFirst(parentdetailObj.getFatherCID());
////            citizenDetailDto.setGuardianNameSecond(citizendetailsObj.getMotherName());
////            citizenDetailDto.setGuardianCidSecond(parentdetailObj.getMotherCID());
//        } else {
//            return ResponseEntity.badRequest().body(new MessageResponse("CID or date of birth is incorrect."));
//        }
        return ResponseEntity.ok(citizenDetailDto);
    }


}
